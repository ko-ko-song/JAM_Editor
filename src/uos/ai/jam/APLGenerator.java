package uos.ai.jam;

import java.util.LinkedList;
import java.util.List;

import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.plan.APL;
import uos.ai.jam.plan.APLElement;
import uos.ai.jam.plan.Plan;
import uos.ai.jam.plan.PlanTable;
import uos.ai.jam.plan.action.Action;
import uos.ai.jam.plan.action.GoalAction;

public class APLGenerator implements Runnable {
	private final IntentionStructure	_intentionStructure;
	private final WorldModelManager		_worldModelManager;
	private final PlanTable				_planLibrary;
	private int 						_metaLevel;
	private APL							_apl;

	public APLGenerator(IntentionStructure intentionStructure, WorldModelManager worldModelManager, PlanTable planLibrary) {
		_intentionStructure		= intentionStructure;
		_worldModelManager				= worldModelManager;
		_planLibrary			= planLibrary;
	}
	
	public APLGenerator setMetaLevel(int metaLevel) {
		_metaLevel = metaLevel;
		return this;
	}
	
	public APL getAPL() {
		return _apl;
	}
	
	public void run() {
		if (JAM.getShowAPL()) {
			System.out.println("JAM::APL: Intention Structure is:\n");
			_intentionStructure.print(System.out);
			System.out.println();
		}
		List<APLElement> intentions = new LinkedList<APLElement>();
		boolean isWorldModelAnyNew = _worldModelManager.getWorldModel().anyNew();

		// Do a quick check to see if anything changed in the World Model.
		// If not then there's nothing to match so simply return.
		if (isWorldModelAnyNew) {
			generateWMBasedAPL(intentions);
		} else {
			if (JAM.getShowAPL()) {
				System.out.println("APL::genWMBasedAPL: No new World Model entries.");
			}
		}
		generateGoalBasedAPL(intentions, isWorldModelAnyNew);
		_apl = new APL(intentions);
		_worldModelManager.getWorldModel().clearNewAll();
	}
	
	private void generateWMBasedAPL(List<APLElement> intentions) {
		/*
		 * 한 사이클 내에 둘 이상의 fact가 변경되는 경우, 단 한번의 selection 후 wm.clearNew()를 호출하는 바람에
		 * 하나를 제외한 나머지 fact들의 conclude가 실행되지 않는 문제 해결
		 */

		for (Relation newFact : _worldModelManager.getWorldModel().getNew()) {
			List<APLElement> temp = new LinkedList<APLElement>();
			for (Plan plan : _planLibrary.getConcludePlans()) {
				Relation concludeRelation = plan.getConcludeSpecification();
				Binding binding = new Binding();
				try {
					if (newFact.unify(concludeRelation, binding, newFact, null)) {
						List<Binding> bl = new LinkedList<Binding>();
						bl.add(binding);
						if (plan.checkContext(bl) && plan.checkPrecondition(bl)) {
							for (Binding b : bl) {
								temp.add(new APLElement(plan, null, b));
							}
						}
					}
				} catch(Throwable ignore) {}
			}
			if (temp.size() > 0) {
				APL apl = new APL(temp);
				APLElement selected = apl.getUtilityRandom();
				try {
					Goal g = _intentionStructure.addUnique((GoalAction) null, (Goal) null, selected.getBinding());
					intentions.add(new APLElement(selected.getPlan(), g, selected.getBinding()));
				} catch (AgentRuntimeException e) {}
			}
		}
	}
	
	private void generateGoalBasedAPL(List<APLElement> intentions, boolean isWorldModelAnyNew) {
		// Go through each possible stack
		for (Goal goal : _intentionStructure.getStacks()) {
			if (JAM.getShowAPL()) {
				System.out.println("APL::genGoalBasedAPL: checking stack w/ top-level goal: " + goal.getName());
				// toplevelGoal.print(System.out);
			}

			// Need to move down to the leaf goal of the intention stack
			while (goal.getSubgoal() != null) {
				if (JAM.getShowAPL()) {
					System.out.println("                                            w/ subgoal: " + goal.getSubgoal().getName());
				}
				goal = goal.getSubgoal();
			}
	
			if (JAM.getShowAPL()) {
				System.out.println("APL::genGoalBasedAPL: Leaf goal of stack is: " + goal.getName());
			}

			if (!goal.generateAPL(isWorldModelAnyNew)) {
				if (JAM.getShowAPL()) {
					System.out.println("APL::genGoalBasedAPL: Skipping goal because generateAPL said no.\n");
				}
				continue;
			}

			// Don't wast time generating an APL if it's a "holder" goal for a CONCLUDE-based intention
			if (goal.getGoalAction() == null) {
				if (JAM.getShowAPL()) {
					System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a CONCLUDE-driven intention.\n");
				}
				continue;
			}

			// Don't waste time generating an APL if it's a satisfied MAINTAIN goal
			// or if it's a MAINTAIN goal and agent is performing metalevel reasoning
			if (goal.getGoalAction().getType() == Action.ACT_MAINTAIN) {
				if (_worldModelManager.getWorldModel().match(goal.getRelation(), null)) {
					if (JAM.getShowAPL()) {
						System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a satisfied MAINTAIN goal\n");
					}
					continue;
				}
				if (_metaLevel >= 1) {
					if (JAM.getShowAPL()) {
						System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a MAINTAIN goal and agent doing meta-reasoning\n");
					}
					continue;
				}
			}
			
			// Go through each possible plan
			// Only consider those plans that have goals (i.e. not CONCLUDE-driven plans)
			List<Plan> plans = _planLibrary.getGoalSpecPlans(goal.getName());
			if (plans != null) {
				for (Plan plan : plans) {
					Binding planBinding = new Binding();
					
					// Filter according to goal type and by ":BY" and ":NOT-BY" lists
					if (goal.getGoalAction() == null || (goal.getGoalAction() != null && !goal.getGoalAction().isEligible(plan, planBinding))) {
						if (JAM.getShowAPL()) {
							System.out.println("APL::genGoalBasedAPL: plan " + plan + " with planBinding " + planBinding + ", is not eligible for goal " + goal);
						}
						continue;
					}
					
					// Now go through each possible plan variable binding and unify goal and plan relations
					Relation planRelation = plan.getGoalSpecification().getRelation();
					if (goal.matchRelation(planRelation, planBinding)) {
						instantiate(intentions, goal, plan, planBinding);
					}
				}
			}
			
			// Maintain goals should always be considered for APL generation.
			// Once a Maintain goal gets intended, then further APL generation
			// will be passed over by the generateAPL() member function above
			// because of the existance of the intention.
			if (goal.getGoalAction() != null && goal.getGoalAction().getType() != Action.ACT_MAINTAIN) {
				goal.clearNew();
			}
		}	
	}

	/**
	 * Go through and find all combinations of variable bindings for the plan/goal combination
	 * 
	 */
	private static void instantiate(List<APLElement> intentions, Goal goal, Plan plan, Binding planBinding) {
		List<Binding> bindingList = new LinkedList<Binding>();
		bindingList.add(planBinding);
		
		if (plan.checkContext(bindingList) && plan.checkPrecondition(bindingList)) {
			for (Binding binding : bindingList) {
				if (goal.isNew() || binding.isNewWMBinding()) {
					if (JAM.getShowAPL()) {
						System.out.println("APL::instantiate: instantiating APL for plan: "
										+ plan + ":\"" + plan.getName() + "\"\n"
										+ "                                     binding: " + binding + "\n"
										+ "                                    and goal: ");
						goal.print(System.out);
					}
					intentions.add(new APLElement(plan, goal, binding));
				}
			}
		}
	}
}
