# TODO(수정중)
비기너 탈출 과제(TODO)

## 참고사항
1. **main branch로는 push하지 말 것**
- main은 develop branch에서만 PR을 통해 merge합니다.(Protection rules로 막아놨는데 혹시 모르니까...)
2. main에 merge하려면 PR에서 1명 이상의 Approve가 필요합니다.
3. 명령어 입력은 Android Studio의 Terminal을 추천합니다.(conflict 되었을 때 코드 수정을 편하게 하기 위해)
---

### Fork 방법
1. 현재 Repository의 우측 상단에 fork 버튼 클릭
2. 원하는 Repository name 설정
3. Copy the main branch only 체크
> 체크하면 main branch만 fork(기본값)
> 체크 해제하면 Repository의 모든 branch를 fork
4. 본인의 Repository에서 clone하여 개발
5. 개발 완료 후에 add, commit, push
---

### Fork한 개인 Github Repository에서 현재 Repository에 변경 사항 적용 방법
> 개인 Repository의 main(또는 원하는 Branch)에서 현재 Repository의 **develop branch**로 Pull Requests 생성
> 또는 현재 Repository에 feature branch를 생성하고 이쪽으로 PR 생성
> 현재 Repository의 main으로는 PR 생성X
---

### Fork한 Repository를 Clone한 이후 초기 설정(동기화 관련)
설정을 하는 이유: 현재 Repository의 변경 사항을 본인이 fork한 Repository에 동기화하기 위해 설정합니다.
1. git remote add {원하는 이름, 보통은 upstream으로 설정} https://github.com/원본사용자명/원본저장소명.git
+ ex) git remote add upstream https://github.com/JaeYoung290/TODO.git
+ git remote -v로 정상적으로 remote 되었는지 확인
+ 1번은 처음 설정할 때만 실행하기
+ 추후 현재 Repository에서 변경 사항을 적용하고 싶을 때는 2번부터 시작
2. git fetch upstream 또는 git fetch upstream {가져올 branch 이름}
3. git checkout {동기화할 본인 branch 이름}
4. git merge upstream/{가져올 branch 이름}
(만약 conflict가 난 상황이라면 코드를 수정한 다음에 add, commit 후에 5번 실행)
5. git push origin {동기화할 본인 branch 이름}

---
### 현재 Repository에 Branch 생성 방법
1. 현재 Repository의 Projects에서 Item 생성
> Item 이름 설정
>> [FEAT]새로운 기능, [BUG]버그 수정, [REFACTOR]코드 리팩토링 등을 접두어로 사용
>> ex)[FEAT]TODO 기능 구현 
3. Assignees는 본인을 설정
4. Convert to issue에서 TODO Repository로 설정
5. Development에서 **Create a branch** 클릭
6. Branch source는 develop branch로 설정
~
---

### 깃 충돌 해결 방법(develop branch에 merge를 진행하던 중 conflict 상황으로 설명)
android studio의 terminal에서 진행할 것(코드 수정을 위해)
다른 브랜치도 방법은 같음(develop만 원하는 branch 이름으로 변경)
+ git checkout develop
+ git pull origin develop
+ git checkout {충돌난 branch 이름}
+ git merge develop
+ 충돌 난 부분 해결
+ 코드 수정 이후 add, commit, push
+ 이후 다시 merge 진행

