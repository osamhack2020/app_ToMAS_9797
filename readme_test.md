Welcome file
김
김
유형
텍스트
크기
16KB (16,444바이트)
사용된 저장용량
16KB (16,444바이트)
위치
mainPage
소유자
나
수정한 날짜
내가 오전 9:04에 수정함
열어본 날짜
내가 오전 9:05에 열어봄
생성한 날짜
StackEdit(으)로 오전 7:50에 작성됨
설명 추가
뷰어가 다운로드할 수 있음
# 프로젝트 소개

  

  

  

군 인트라넷 내의 아미누리 참여마당과 공군 IT 게시판을 보면서 군이라는 일체감 아래에서 이루어내는 시너지가 가지고 있는 가능성을 보았습니다. 그리고 그 가능성을 군 내부가 아닌 외부에서도 이용한다면 괄목할 만한 성과가 있을 것이라 생각했기에 이 프로젝트를 시작하게 되었습니다.

  

  

  

![아미누리](https://user-images.githubusercontent.com/72395020/97163474-0b90cd00-17c4-11eb-9c13-c1210e90a59e.PNG)

  

  

  

하지만 이러한 시너지는 다양한 전공 , 직업의 사람들이

  

군대라는 이름 아래에 모인다는 군의 특수성에 기반해 있었기 때문에

  

군 외부에서도 시너지를 이끌어 내고 활성화 시키려면 두 가지 조건을 충족 시켜야 할 필요가 있었습니다.

  

  

**1. 군인이거나 군인 이었다는 경험을 가지고 있는 사람들이 이용해야 한다.

  

2. 군인들이 개인적인 시간을 할애해서 이용할 만한 가치가 있어야 한다.**

  

  

  

1번 조건같은 경우는 소속 부대의 정보를 받고 군번을 받음으로써 어느 정도는 해결이 되었습니다.

  

  

*하지만* 2번 조건같은 경우는 해결하기가 그리 쉽지 않았습니다.

  

아미누리 같은 사이트를 만들기 위해 게시판을 생성하는 기능은 필수인데, 이 게시판 기능만 제공한다면 기존의 다른 어플들에 비해 차별성을 갖추지못하고 , 차별성을 갖추지 못한 어플은 가치가 없기 때문입니다.

  

  

따라서 게시판 기능만큼 비중을 가진,오히려 더 큰 비중을 가진 기능들을 제공하여 이 어플리케이션의 가치를 창출하기로 하였습니다.

  

  

  

## ToMAS의 기능

  

  

**자기개발,소통 게시판**

  

자기개발 게시판은 하부항목으로 수능,자격증,전공,언어시험이 있습니다. 또한 관리자가 *동적으로 게시판을 추가하고 제거할 수 있는 기능*을 제공하여 사용자의 선호에 맞춰 게시판을 조정 할 수 있습니다.

[사진추가 or 동적 기능 강조]

  
  

**1. 동적으로 서버에서 구조를 받아와 구성**

내용

  

게시판 목록을 firestore 서버에서 불러와서 구성한다. 이를 통해 확장성을 확보할 수 있었다.

  

기술

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

각 구조 field에 'fragment_style'로 저장되어 있는 값을 불러와서 게시판 자식 목록을 구성한다.

fragment_template.java에서 firestore 게시판 구조 접근 'path'와 'fragment_style'을 부모 fragment_template.java로 부터 Bundle argument로 받아 내용물을 구성한다. 이를 통해 동적으로 fragment를 구성해서 관리자가 firestore에 'fragment_style'을 기입한 document를 추가하기만 하면 손쉽게 게시판을 추가할 수 있다.

</div>

</details>

  

**2. Html wysiwyg editor를 사용하였다.**

사진 + 글 조합으로 글을 쓸 수 있는 기능을 제공하기 위해 Html wysiwyg editor를 사용하였다. 자기개발을 위한 질문이나 소통을 하기 위해서는 사진 따로 글 따로 구성하는 것 보다 사진과 글을 자유롭게 배치 할 수 있는 것이 효과적이다.

  

기술

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

  

</div>

</details>

https://github.com/irshuLx/Android-WYSIWYG-Editor

**3. 댓글 쓰기 기능을 제공한다.**

글쓰기 기능과 동일하나, 해당 글 아래에 보여준다. 원본 글을 쓴 사람이 추천 댓글을 지정할 수 있게 한다. 이 때 추천 댓글로 지정된 사람은 플리마켓에서 사용할 수 있는 point를 받는다.

  

**4. 수정, 삭제 기능을 제공한다.**

게시판 기능에서 수정, 삭제 기능은 당연한 겁니다.

기술

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

register_board_content.java를 불러올 때 신규 글 쓰기의 경우 post_id를 인자로 넘겨주지 않고, 수정하기 경우엔 해당 글의 post_id을 인자로 넘겨주어서, If문을 통해 분기한다. 수정하기 일 때는 기존에 썼던 내용을 editor에 넣어준다. 글을 서버에 올릴 때는 신규 글 쓰기의 경우 새로운 uuid를 통해 post_id를 정해주고, 수정하기의 경우 기존의 post_id에 set()을 하여 업데이트한다.

```gradle

// 새로 글 쓸 때

  

if (post_id.equals("")) {

  

db.collection(path).document()

  

.set(post)

  

.addOnSuccessListener(new OnSuccessListener<Void>() {

  

@Override

  

public void onSuccess(Void aVoid) {

  

setResult(Activity.RESULT_OK);

  

finish();

  

}

  

})

  

.addOnFailureListener(new OnFailureListener() {

  

@Override

  

public void onFailure(@NonNull Exception e) {

  

}

  

});

  

}

  

else

  

{// 수정할 때

  

db.collection(path).document(post_id)

  

.set(post)

  

.addOnSuccessListener(new OnSuccessListener<Void>() {

  

@Override

  

public void onSuccess(Void aVoid) {

  

setResult(Activity.RESULT_OK);

  

finish();

  

}

  

})

  

.addOnFailureListener(new OnFailureListener() {

  

@Override

  

public void onFailure(@NonNull Exception e) {

  

}

  

});

  

}

```

  

삭제하기의 경우 firestore 자체에서 collection이 포함된 document 삭제를 제공하지 않았다.

우리 글 구조에는 댓글 collection까지 포함되어 있었기 때문에 댓글 document까지 삭제하는 것을 client에서 처리하였다.

```

mPostReference.collection("comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

  

@Override

  

public void onComplete(@NonNull Task<QuerySnapshot> task) {

  

if (task.isSuccessful()) {

  

for (QueryDocumentSnapshot document : task.getResult()) {

  

mPostReference.collection("comments").document(document.getId()).delete();

  

}

  

mPostReference.delete();

  

} else {

  

//Log.d(TAG, "Error getting documents: ", task.getException());

  

}

  

}

  

});

  

fragmentManager.beginTransaction().remove(board_content.this).commit();

  

fragmentManager.popBackStack();

```

</div>

</details>

  

**5. 게시판의 형식을 parameter를 통해 결정한다.**

ToMAS에서 제공하는 게시판 목록 형식은 다음과 같다.

[1.2.3 사진넣기]

1번은 기본적인 하부 게시판 목록만을 불러오는 것이다.

2번은 하부 게시판 목록과 함께 하부 게시판에 포함되어 있는 게시물들을 5개씩 보여준다.

3번은 게시물 목록을 보여준다.

  

2번 fragment_style은 한 번에 다양한 하위 게시판의 게시물을 보는게 유리한 상황에서 사용하였다. 
ex) 자기개발 2번째, 플리마켓의 첫 번째 사진 넣기

구조 상 2번 다음은 항상 3번이 나와야 하지만, 1번 fragment_style은 계속해서 사용할 수 있다. 이를통해 무한한 게시판 확장이 가능하다.

  

소통게시판은 장병들의 취미에 관련된 지식을 공유하는 게시판 기능을 제공합니다. 기본 하부 항목으로는 IT,운동,음악,영화,독서가 있고 자기개발 게시판과 마찬가지로 동적으로 게시판을 추가,제거 할 수 있는 기능을 제공합니다.

  

[사진추가]

  

  

**플리마켓**

  

  

부대 내에서 사용하지 않는 물품들을 플리마켓의 형식으로 거래 할 수 있는 기능을 제공합니다. 플리마켓 내에서 사용자는 판매자와 구매자로 구분이 됩니다. 판매자는 정해진 기간동안 가장 높은 포인트를 제시하는 구매자와 물품을 거래 합니다. 거래방식은 직거래로 한정하고 영내부대 단위로 마켓게시판을 구성하여 다른 부대와의 혼동을 방지합니다. 거래 시 구매자가 마이페이지에 있는 수령 완료 버튼을 누르면 포인트가 결산됩니다.

특징
1. 소속부대 단위로 데이터베이스 분리
<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

다른부대 사용자와의 혼선을 막고 이동 가능한 영내 단위에서의 거래를 원활하게 한다.
기술
<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">
소속 선택을 처리하기 위해서 RecyclerTreeView 라이브러리를 이용했습니다.
서버에서 소속 구조를 받아와 RecyclerTreeView를 구성합니다. 이를 통해 firestore에 구조 하나만 추가해서 새로운 소속의 부대를 추가할 수 있습니다.
```
public void init_tree(TreeNode<Dir> node)  
{  
    // 자기자신의 path까지 node로 저장하고 이를 firebase path에 넘겨줌.  
  FirebaseFirestore.getInstance().collection(node.getPath())  
            .get()  
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
                @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                    if (task.isSuccessful()) {  
                        for (QueryDocumentSnapshot document : task.getResult()) {  
                            String tmp_path = node.getPath() + "/" + document.getId() + "/" + document.getId();  
  TreeNode<Dir> tmp_node = new TreeNode<>(new Dir(document.getId()), tmp_path);  
  init_tree(tmp_node);  
  node.addChild(tmp_node);  
  }  
                        if (check_first) {  
                            adapter.refresh(nodes);  
  check_first = false;  
  }  
                    } else {  
                    }  
                }  
            });  
}
```
</div>

</details>

</div>

</details>

2. 기간 내 최고가 입찰자가 수령 완료 버튼을 누르면 포인트 결산

3. 카테고리별로 물품 구성
카테고리 별로 물품을 분할하여 등록하게 해서 필요한 물건을 쉽게 찾을 수 있게 한다.

4. 글 + 그림 조합으로 글 작성


  

[사진추가]

  

  

**인원모집**

  

인원이 여러 명 필요한 활동을 위해 인원을 모집 할 수 있는 기능을 제공합니다.

  

하부항목으로는 운동,동아리,대회가 있습니다.

  

운동 인원모집을 예로 들면 날짜,시간,위치,운동종목,인원을 설정해서 모집신청을 할 수 있습니다.

  

[사진추가]

  

  

**일정표**

  

근무,휴가,훈련 등의 개인적인 일정을 달력에 기록하는 기능을 제공합니다. 소속부대의 중간관리자가 부대 훈련 일정을 추가하면 부대원들의 개인일정표에 자동으로 업데이트 하는 기능을 제공합니다.

  

  

**소속부대 게시판**

  

중대와 대대 단위로 설문조사 기능과 공지사항 기능을 제공합니다. 소속부대의 중간관리자가 설문조사와 공지사항을 등록 할 수 있게 제공되어 있습니다. 필수 공지사항 같은 경우는 부대원들이 확인을 했는지 안했는지를 표현해주는 기능을 만들어서 중간관리자가 부대원들의 내용 확인 여부를 체크 할 수 있게합니다.

  

  

  

일반적으로 위처럼 모션을 만들기 위해 각 1000 라인으로 구성된 코드가 사용되어야 하는데

  

  

물리적인 좌표 연산과 View를 표현하기 위한 알고리즘들이 필요하기 때문입니다.

  

  

더 많은 것을 해야 한다면 게임엔진을 사용해야 할 것입니다.

  

  

게임 엔진을 사용한다면 기술을 습득해야하는 어려움과 Android의 API, 리소스를 사용할수 없게 되어 버립니다.

  

  

  

### [](https://github.com/muabe/Propose#propose%EB%8A%94-%EC%89%BD%EA%B2%8C-%EB%AA%A8%EC%85%98-%EA%B5%AC%ED%98%84%EC%9D%B4-%EA%B0%80%EB%8A%A5%ED%95%9C-touch-animatoin-%EC%97%94%EC%A7%84-%EC%9E%85%EB%8B%88%EB%8B%A4)Propose는 쉽게 모션 구현이 가능한 Touch Animatoin 엔진 입니다

  

  

  

----------

  

  

  

다시 말해 Propose는 동적-인터랙션을 구현하기 위한 API를 제공합니다.

  

  

Propose를 사용한다면 몇 줄의 코드만으로 모션을 만들수 있습니다.

  

  

아래 소개영상으로 얼마나 쉽게 모션을 개발할 수 있는지 알 수 있습니다.

  

  

  

> #### [](https://github.com/muabe/Propose#%EC%86%8C%EA%B0%9C-%EC%98%81%EC%83%81%EB%B3%B4%EA%B8%B0-)[소개 영상보기](https://youtu.be/v0gIuIK3Ww4)

  

  

  

[![Android Propose Story book](https://github.com/muabe/Minor-League/raw/master/images/propose/book%20flip.png)](https://youtu.be/v0gIuIK3Ww4)

  

  

  

### [](https://github.com/muabe/Propose#%EC%9E%A5%EC%A0%90-%EB%B0%8F-%ED%99%9C%EC%9A%A9%EB%B6%84%EC%95%BC)장점 및 활용분야

  

  

  

----------

  

  

  

터치 모션의 일반적인 개발 방법으로 canvas를 재정의하고 터치 연산 등 상당한 개발 비용이 소모됩니다.

  

  

Propose를 사용한다면 쉬운 모션 구현과 간단한 코드 유지관리가 가능합니다.

  

  

아래는 3D Flip 화면 개발시 일반 개발과 Propose를 비교한 도표 입니다.

  

  

  

> 일반 개발 VS Propose

  

  

  

3D Flip 구현

  

  

  

일반 개발

  

  

  

Propose

  

  

  

개발 난이도

  

  

  

고급 개발자

  

  

  

**초급 개발자**

  

  

  

코드 라인수

  

  

  

500~100 라인

  

  

  

**10~30 라인**

  

  

  

재사용/확장성

  

  

  

전체 소스코드 수정

  

  

  

**애니메이션 추가 및 조합**

  

  

  

안정성

  

  

  

Thread 자체관리(에러율 높음)

  

  

  

**Android SDK에서 Thread 자동관리**

  

  

  

```

  

  

Propose는 고급스럽고 사용자 친화적 모션(부드럽게) 구현과 모바일 리소스 최소화,

  

  

개발자 휴먼 에러 최소화, 구현 시간 단축 및 개발비용 절감 효과를 기대할 수 있습니다.

  

  

특히 기업앱의 고급화, 게임, 이북(동화), 앨범앱 제작 등등

  

  

동적기능 필요한 다양한 분야로 활용될 수 있습니다.

  

  

  

```

  

  

  

  

### [](https://github.com/muabe/Propose#propose-%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD)Propose 개발 환경

  

  

  

----------

  

  

  

개발환경

  

  

  

Android SDK 3.0 이상

  

  

  

  

### [](https://github.com/muabe/Propose#propose-import)Propose Import

  

  

  

----------

  

  

  

Android Gradle 파일에 아래 코드를 추가하여 라이브러리를 Import할 수 있습니다

  

  

  

```

  

  

compile 'com.markjmind.propose:propose:1.1.+'

  

  

  

```

  

  

  

> Note : Gradle은 Android의 defalut 빌드툴이며 Propose는 JCenter에서 자동 다운로드 됩니다.

  

  

  

  

### [](https://github.com/muabe/Propose#%EA%B0%9C%EB%B0%9C%EB%AC%B8%EC%84%9C)개발문서

  

  

  

----------

  

  

  

- [Propose 개요](https://github.com/muabe/Propose/wiki/1.-Propose-%EA%B0%9C%EC%9A%94)

  

  

- [개발 가이드](https://github.com/muabe/Propose/wiki/2.-%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0)

  

  

- [아키텍쳐](https://github.com/muabe/Propose/wiki/architecture)

  

  

- [클래스 다이어그램](https://github.com/muabe/Propose/wiki/Class-Diagram)

  

  

- [JavaDoc](http://muabe.github.io/Propose/)

  

  

- [Sample](https://github.com/muabe/Propose/wiki/Samples)

  

  

  

  

### [](https://github.com/muabe/Propose#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B4%80%EB%A6%AC)프로젝트 관리

  

  

  

----------

  

  

  

- [Trello](https://trello.com/b/pYiqclvp/propose)

  

  

- [Issues](https://github.com/muabe/Propose/issues)

  

  

- [_QnA_](https://github.com/muabe/Propose/issues/new)

  

  

  

  

### [](https://github.com/muabe/Propose#license)LICENSE

  

  

  

----------

  

  

  

This copy of Propose is licensed under the

  

  

Lesser General Public License (LGPL), version 2.1 ("the License").

  

  

See the License for details about distribution rights, and the

  

  

specific rights regarding derivate works.

  

  

You may obtain a copy of the License at:

  

  

[http://www.gnu.org/licenses/licenses.html](http://www.gnu.org/licenses/licenses.html)

  

  

  

https://github.com/muabe/Propose멘토깃허브링크
<!--stackedit_data:
eyJoaXN0b3J5IjpbNDc3NDY5MTAwLDE4MjYwNzgyLC03NzQ3OD
U3MDMsLTEyNjA5OTA4NjAsLTIxMjA0MzAyNTldfQ==
-->
