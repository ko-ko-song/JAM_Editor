eclipse version : 2018-12

프로젝트 아래에 plan or plans folder에 있는 jam 파일의 삭제, 변경, 수정 등을 Listen함
==> jam 파일은 MyProject/plan 아래에 있어야함 이 아래 폴더 depth는 상관 없음


기능
* text highlight 				
* syntax error detect  				- problem으로 표시
* relation error detect   				- warning으로 표시
* content assist(자동완성)   				- (relation, action)
* hyperlink(relation에 Ctrl + 클릭 시 해당 plan file open)




[bug fix]
Token error 발생 시
Execption 으로 못잡음 TokenErrExecption으로 따로 잡아야 함... what???


IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
이상한 path에서 폴더 가져와도 폴더 객체가 만들어짐...
당연히 path == null이면으로 예외 처리를 해놨는디
folder.exists() 이걸로 해야 함......


[플러그인 수동 추가]
1. eclipse\configuration\org.eclipse.equinox.simpleconfigurator\bundles.info에 아래 구문 추가
   식별자,버전,file,4,false
   ex) 
   kr.ac.uos.ai.editor.jamEditor,1.0.0.4,plugins/kr.ac.uos.ai.editor.jamEditor_1.0.0.4.jar,4,false

2. eclipse\plugins에
   플러그인 파일(.jar) 추가

정상적으로 플러그인이 동작하지 않을 경우
~/your_workspace_path/.metadata/.log 확인해보기
