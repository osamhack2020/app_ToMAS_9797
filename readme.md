  
# 프로젝트 소개

  

  

  

군 인트라넷 내의 아미누리 참여마당과 공군 IT 게시판을 보면서 군이라는 일체감 아래에서 이루어내는 시너지가 가지고 있는 가능성을 보았습니다. 그리고 그 가능성을 군 내부가 아닌 외부에서도 이용한다면 괄목할 만한 성과가 있을 것이라 생각했기에 이 프로젝트를 시작하게 되었습니다.

  

  

  

![아미누리](https://user-images.githubusercontent.com/72395020/97163474-0b90cd00-17c4-11eb-9c13-c1210e90a59e.PNG)

  

  

  

하지만 이러한 시너지는 다양한 전공 , 직업의 사람들이

  

군대라는 이름 아래에 모인다는 군의 특수성에 기반해 있었기 때문에

  

군 외부에서도 시너지를 이끌어 내고 활성화 시키려면 두 가지 조건을 충족 시켜야 할 필요가 있었습니다.

  

  

1. 군인이거나 군인 이었다는 경험을 가지고 있는 사람들이 이용해야 한다.

  

2. 군인들이 개인적인 시간을 할애해서 이용할 만한 가치가 있어야 한다.

  

  

  

1번 조건같은 경우는 소속 부대의 정보를 받고 군번을 받음으로써 어느 정도는 해결이 되었습니다.

  

  

*하지만* 2번 조건같은 경우는 해결하기가 그리 쉽지 않았습니다.

  

아미누리 같은 사이트를 만들기 위해 게시판을 생성하는 기능은 필수인데, 이 게시판 기능만 제공한다면 기존의 다른 어플들에 비해 차별성을 갖추지못하고 , 차별성을 갖추지 못한 어플은 가치가 없기 때문입니다.

  

  

따라서 게시판 기능만큼 비중을 가진,오히려 더 큰 비중을 가진 기능들을 제공하여 이 어플리케이션의 가치를 창출하기로 하였습니다.

  

  

  


# ToMAS의 기능



## 1) 자기개발,소통 게시판

  

자기개발 게시판은 하부항목으로 수능,자격증,전공,언어시험이 있습니다. 또한 관리자가 *동적으로 게시판을 추가하고 제거할 수 있는 기능*을 제공하여 사용자의 선호에 맞춰 게시판을 조정 할 수 있습니다.

소통게시판은 장병들의 취미에 관련된 지식을 공유하는 게시판 기능을 제공합니다. 기본 하부 항목으로는 IT,운동,음악,영화,독서가 있고 자기개발 게시판과 마찬가지로 동적으로 게시판을 추가,제거 할 수 있는 기능을 제공합니다.

[사진추가 or 동적 기능 강조]

  
  
### 1. 동적으로 서버에서 구조를 받아와 구성

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">
  

게시판 목록을 firestore 서버에서 불러와서 구성합니다. 이를 통해 확장성을 확보할 수 있습니다.

 

<details>

<summary>기술 자세히</summary>
<div markdown="1">

```java
public void show_field()  
{  
    switch (fragment_style)  
    {  
        case 1:  
            root = inflater.inflate(R.layout.template1, container, false);  
  ListView tmp_listview = (ListView)root.findViewById(R.id.template1_listView);  
 final ArrayAdapter<String> listview_adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_list_item_1, child_list); // simple_list_item layout를 바꿔야 style이 바뀜  
  tmp_listview.setAdapter(listview_adapter);  
  // 구조 다시 바꿈. fragment_template에서 child_list를 firebase에서 읽어옴.  
 // firebase에서 child_list채우기  
  mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
            @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                if (task.isSuccessful()) {  
                    child_list.clear();  
  child_fragment_list.clear();  
 for (QueryDocumentSnapshot document : task.getResult()) {  
                        //Log.d(TAG, document.getId() + " => " + document.getData());  
  child_list.add(document.getId());  
  child_fragment_list.add(document.get("fragment_style", Integer.class));  
  }  
                    listview_adapter.notifyDataSetChanged();  
  } else {  
                    //Log.d(TAG, "Error getting documents: ", task.getException());  
  }  
            }  
        });  
  tmp_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
                @Override  
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {  
                    fragmentTransaction = fragmentManager.beginTransaction();  
  fragmentTransaction.addToBackStack(null);  
  // 다음 child를 만들고 arg 넘기는 과정  
  // need to fix db에서 받아와서 분기해야댐.  
  Fragment change_fragment = new FragmentTemplate();  
  Bundle args = new Bundle();  
  args.putInt("fragment_style", child_fragment_list.get(i));  
  args.putString("title", child_list.get(i));  
  args.putString("path", path);  
  change_fragment.setArguments(args);  
  fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();  
  }  
            });  
 break; case 2:  
            root = inflater.inflate(R.layout.template2, container, false);  
 final ListView template2_list = root.findViewById(R.id.template2_list);  
  
  // custom listview를 생성해서 만들어야됨.  
  final Template2ListAdapter template2_adapter = new Template2ListAdapter(mainActivity, fragmentManager);  
  template2_list.setAdapter(template2_adapter);  
  // fragment_style 2에서는 template2_list_adapter에서 클릭을 처리한다.  
  
 // firestore에서 subject list 불러오기.  
  mainActivity.db.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
                @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                    if (task.isSuccessful()) {  
                        for (QueryDocumentSnapshot document : task.getResult()) {  
                            String tmp = document.getId();  
 final BoardListAdapter tmp_sample_list_adapter = new BoardListAdapter();  
  // firestore sample list 불러오기  
  mainActivity.db.collection(path + "/" + tmp + "/" + tmp).orderBy("timestamp", Query.Direction.DESCENDING).limit(5)  
                                    .get()  
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
                                        @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                                            if (task.isSuccessful()) {  
                                                for (QueryDocumentSnapshot document : task.getResult()) {  
                                                    Log.d("QQQ", document.get("title").toString());  
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
  String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());  
  tmp_sample_list_adapter.addItem(document.get("title").toString(), document.get("num_comments").toString(), dateString, document.get("writer").toString(), document.get("clicks").toString(), document.getId());  
  }  
                                                tmp_sample_list_adapter.notifyDataSetChanged();  
  } else {  
                                                //Log.d(TAG, "Error getting documents: ", task.getException());  
  }  
                                        }  
                                    });  
  template2_adapter.addItem(tmp, tmp_sample_list_adapter, path);  
  }  
                        template2_adapter.notifyDataSetChanged();  
  
  } else {  
                        //Log.d(TAG, "Error getting documents: ", task.getException());  
  }  
                }  
            });  
 break; case 3:  
            root = inflater.inflate(R.layout.template3, container, false);  
  ListView tmp3_listview = root.findViewById(R.id.template3_listView);  
 final BoardListAdapter adapter = new BoardListAdapter();  
  tmp3_listview.setAdapter(adapter);  
  
  // need to fix addItem에 서버에서 받아온 db를 넣어야 함.  
  mainActivity.db.collection(path).orderBy("timestamp", Query.Direction.DESCENDING)  
                .get()  
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
                    @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                        if (task.isSuccessful()) {  
                            for (QueryDocumentSnapshot document : task.getResult()) {  
                                Log.d("AAA", document.get("title").toString());  
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
  String dateString = formatter.format(document.get("timestamp", Timestamp.class).toDate());  
  adapter.addItem(document.get("title").toString(), document.get("num_comments").toString(), dateString, document.get("writer").toString(), document.get("clicks").toString(), document.getId());  
  }  
                            adapter.notifyDataSetChanged();  
  } else {  
                            //Log.d(TAG, "Error getting documents: ", task.getException());  
  }  
                    }  
                });  
  
  // click함수에서 key값을 넘겨서 게시판 db에서 가져온 데이터를 넣어야 함.  
  tmp3_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
                @Override  
  public void onItemClick(AdapterView parent, View v, int position, long id) {  
                    fragmentTransaction = fragmentManager.beginTransaction();  
  fragmentTransaction.addToBackStack(null);  
  Fragment change_fragment = new BoardContent();  
  // 게시판 id와 path를 받아와서 board_content fragment로 넘긴다.  
 // maybe 이거 구조를 검색해서 바꿔야 할 듯  
  Bundle args = new Bundle();  
  args.putString("post_id", adapter.listViewItemList.get(position).getId());  
  args.putString("path", path);  
  change_fragment.setArguments(args);  
  fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();  
  }  
            });  
  
  // fab버튼 관리  
  FloatingActionButton fab = root.findViewById(R.id.fab_board_register);  
  fab.setOnClickListener(new View.OnClickListener() {  
                @Override  
  public void onClick(View view) {  
                    Intent intent = new Intent(mainActivity, RegisterBoardContent.class);  
  intent.putExtra("path", path);  
  intent.putExtra("post_id", "");  
  startActivityForResult(intent, 11111);  
  }  
            });  
  
 break; default:  
            break;  
  }  
}
```

![board_firestore_example](https://github.com/rlarla915/readme_sample/blob/main/board_firestore_example.PNG)

각 구조 field에 `fragment_style`로 저장되어 있는 값을 불러와서 게시판 자식 목록을 구성합니다.

`path`와 `fragment_style`을 부모 `FragmentTemplate.java`로 부터 Bundle argument로 받아 내용물을 구성합니다.  `path`는 현재 게시판 목록의 firestore에서의 위치를 변수로 갖습니다. 각 `fragment_style`일 때 필요한 값을 firestore에서 불러와서 리스트 adapter에 추가하고, `Adpater.notifyDataSetChanged()`함수를 통해 바뀐 list를 보여줍니다.
관리자가 firestore에 `fragment_style`을 기입한 document를 추가하기만 하면 손쉽게 게시판을 추가할 수 있습니다.

---

</div>

</details>

---

</div>

</details>

  

### 2. Html wysiwyg editor를 사용


<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

사진 + 글 조합으로 글을 쓸 수 있는 기능을 제공하기 위해 Html wysiwyg editor를 사용했습니다. 자기개발을 위한 질문이나 소통을 하기 위해서는 사진 따로 글 따로 구성하는 것 보다 사진과 글을 자유롭게 배치 할 수 있는 것이 효과적이라 생각했습니다.

  

<details>

<summary>기술 자세히</summary>

<div markdown="1">

  
사용 라이브러리 : https://github.com/irshuLx/Android-WYSIWYG-Editor

```gradle
implementation 'com.github.irshulx:laser-native-editor:3.0.3'
```

```java
@Override  
  public void onUpload(Bitmap image, final String uuid) {  
        Toast.makeText(RegisterBoardContent.this, uuid, Toast.LENGTH_LONG).show();  
  ByteArrayOutputStream baos = new ByteArrayOutputStream();  
  image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
  storageRef = storage.getReference().child("images/"+uuid+".jpg");  
  
 final byte[] data = baos.toByteArray();  
  
  UploadTask uploadTask = storageRef.putBytes(data);  
  
  Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {  
            @Override  
  public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {  
                if (!task.isSuccessful()) {  
                    throw task.getException();  
  }  
  
                // Continue with the task to get the download URL  
  return storageRef.getDownloadUrl();  
  }  
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {  
            @Override  
  public void onComplete(@NonNull Task<Uri> task) {  
                if (task.isSuccessful()) {  
                    Uri downloadUri = task.getResult();  
  editor.onImageUploadComplete(downloadUri.toString(), uuid);  
  } else {  
                    // Handle failures  
 // ...  }  
            }  
        });  
}
```

editor에서 이미지를 앨범에서 불러와 넣을 때, Html코드 `<img src =""/>`로 변환되어 들어갑니다. 이때 firestore내에 이미지를 저장한 뒤, `getDownloadUrl()`함수를 통해 url을 받고 이를 `src` 안에 넣어줍니다.

---

</div>

</details>

---

</div>

</details>


### 3. 댓글 쓰기 기능을 제공

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

글쓰기 기능과 동일하나, 해당 글 아래에 보여준다. 원본 글을 쓴 사람이 추천 댓글을 지정할 수 있게 합니다. 이 때 추천 댓글로 지정된 사람은 플리마켓에서 사용할 수 있는 point를 받습니다.



<details>

<summary>기술 자세히</summary>

<div markdown="1">

---  

</div>

</details>

---

</div>

</details>
  

### 4. 수정, 삭제 기능을 제공

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

게시판 기능에서 수정, 삭제 기능은 당연한 겁니다.


<details>

<summary>기술 자세히</summary>

<div markdown="1">

register_board_content.java를 불러올 때 신규 글 쓰기의 경우 post_id를 인자로 넘겨주지 않고, 수정하기 경우엔 해당 글의 post_id을 인자로 넘겨주어서, If문을 통해 분기합니다. 수정하기 일 때는 기존에 썼던 내용을 editor에 넣어줍니다. 글을 서버에 올릴 때는 신규 글 쓰기의 경우 새로운 uuid를 통해 post_id를 정해주고, 수정하기의 경우 기존의 post_id에 set()을 하여 업데이트합니다.

```java
// 새로 글 쓸 때  
if (post_id.equals("")) {  
    db.collection(path).document()  
            .set(post)  
            .addOnSuccessListener(new OnSuccessListener<Void>() {  
                @Override  
  public void onSuccess(Void aVoid) {  
                    setResult(Activity.RESULT_OK);  
  finish();  
  Log.d("AAA", "DocumentSnapshot successfully written!");  
  }  
            })  
            .addOnFailureListener(new OnFailureListener() {  
                @Override  
  public void onFailure(@NonNull Exception e) {  
                    Log.w("AAA", "Error writing document", e);  
  }  
            });  
}  
else {// 수정할 때  
  db.collection(path).document(post_id)  
            .set(post)  
            .addOnSuccessListener(new OnSuccessListener<Void>() {  
                @Override  
  public void onSuccess(Void aVoid) {  
                    setResult(Activity.RESULT_OK);  
  finish();  
  Log.d("AAA", "DocumentSnapshot successfully written!");  
  }  
            })  
            .addOnFailureListener(new OnFailureListener() {  
                @Override  
  public void onFailure(@NonNull Exception e) {  
                    Log.w("AAA", "Error writing document", e);  
  }  
            });  
}
```

  

삭제하기의 경우 firestore 자체에서 collection이 포함된 document 삭제를 제공하지 않았습니다.

저희의 구조에는 댓글 collection까지 포함되어 있었기 때문에 댓글 document까지 삭제하는 것을 client에서 처리하였습니다.

```java
delete_button.setOnClickListener(new View.OnClickListener() {  
    @Override  
  public void onClick(View view) {  
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
  fragmentManager.beginTransaction().remove(BoardContent.this).commit();  
  fragmentManager.popBackStack();  
  }  
});
```
---
</div>

</details>

---

</div>

</details>


  

### 5. 게시판의 형식을 parameter를 통해 결정

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">


ToMAS에서 제공하는 게시판 목록 형식은 다음과 같다.

[1.2.3 사진넣기]

1번은 기본적인 하부 게시판 목록만을 불러오는 것이다.

2번은 하부 게시판 목록과 함께 하부 게시판에 포함되어 있는 게시물들을 5개씩 보여준다.

3번은 게시물 목록을 보여준다.

  

2번 fragment_style은 한 번에 다양한 하위 게시판의 게시물을 보는게 유리한 상황에서 사용하였다. 
ex) 자기개발 2번째, 플리마켓의 첫 번째 사진 넣기

구조 상 2번 다음은 항상 3번이 나와야 하지만, 1번 fragment_style은 계속해서 사용할 수 있다. 이를통해 무한한 게시판 확장이 가능하다.



<details>

<summary>기술 자세히</summary>

<div markdown="1">

  
---

</div>

</details>

---
  
</div>

</details>

  

## 2) 플리마켓

부대 내에서 사용하지 않는 물품들을 플리마켓의 형식으로 거래 할 수 있는 기능을 제공합니다. 플리마켓 내에서 사용자는 판매자와 구매자로 구분이 됩니다. 판매자는 정해진 기간 동안 가장 높은 포인트를 제시하는 구매자와 물품을 거래 합니다. 거래방식은 직거래로 한정하고 영내부대 단위로 마켓게시판을 구성하여 다른 부대와의 혼동을 방지합니다. 거래 시 구매자가 마이페이지에 있는 수령 완료 버튼을 누르면 포인트가 결산됩니다.

ToMAS의 플리마켓은 다른 중고거래 서비스와 비교했을 때 군 내에서는 압도적인 경쟁력이 있습니다.
*군복 및 군용장구의 단속에 관한 법률* 에 따라 군복을 입지 못하는 민간인에게 군복 및 군용장구를 판매해서는 안됩니다. 또한 다른 중고거래 서비스는 초점이 군인에 맞춰져있지 않으므로 거래장소와 거래 물품에 있어 제한사항이 많습니다.

*특징*

### 1. 부대 단위로 데이터베이스 분리
<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">



다른부대 사용자와의 혼선을 막고 이동 가능한 영내 단위에서의 거래를 원활하게 한다.


<details>

<summary>기술 자세히</summary>

<div markdown="1">

사용 라이브러리 : https://github.com/TellH/RecyclerTreeView 


소속 선택을 처리하기 위해서 RecyclerTreeView 라이브러리를 이용했습니다.
서버에서 소속 구조를 받아와 RecyclerTreeView를 구성합니다. firestore에 구조 하나만 추가해서 새로운 소속의 부대를 추가할 수 있습니다.
`BelongTreeDialog.java`

```java
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
라이브러리의 `TreeNode<Dir> node`를 커스터마이징해서 소속 부대의 path를 저장할 수 있는 객체로 만들었습니다. firestore의 소속 부대 구조에 자식 document가 있다면, 계속 탐색해서 새로운 `node`를 생성하고 부모 `node`에 연결합니다.
터치 시엔 view의 background에 색을 줘서 어떤 것이 마지막에 선택되었는지 체크할 수 있게 합니다. 이후 확인 버튼을 눌러 Dialog를 끄게 되면, 마지막에 눌렸던 `node`의 path를 `getPath()`를 통해 불러와 `BelongTreeDialog`를 호출한 fragment에 path값을 전달해주었습니다.  이는 `interface`를 통해 구현되었습니다.

---

</div>

</details>

---

</div>

</details>


### 2. 최고가 입찰자가 수령 완료 버튼을 누르면 포인트 결산

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

[마이페이지 내 수령완료 게시판 사진 추가]
위 그림과 같이 최고 입찰자는 판매자가 정해논 기간이 지나면 마이페이지의 구매 확정 목록에 리스트 항목이 생깁니다. 이 리스트 항목을 보고, 


<details>

<summary>기술 자세히</summary>

<div markdown="1">

  ---
  
</div>

</details>

---

</div>

</details>


### 3. 카테고리별로 물품 구성


<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

카테고리 별로 물품을 분할하여 등록하게 해서 필요한 물건을 쉽게 찾을 수 있게 한다.
[marketFragment 이미지 추가]


<details>

<summary>기술 자세히</summary>

<div markdown="1">

  ---
  
</div>

</details>

  ---
  
</div>

</details>


### 4. 글 + 그림 조합으로 글 작성


<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">




<details>

<summary>기술 자세히</summary>

<div markdown="1">

  ---
  
</div>

</details>

  ---
  
</div>

</details>
  

  

## 3) 인원모집

 
인원이 여러 명 필요한 활동을 위해 인원을 모집 할 수 있는 기능을 제공합니다.

하부항목으로는 운동,동아리,대회가 있습니다.
운동 인원모집을 예로 들면 날짜,시간,위치,운동종목,인원을 설정해서 모집신청을 할 수 있습니다.

인원모집 기능은 영내에서 가장 필요한 기능 중 하나라고 생각합니다.
군인 특성 상 간부를 포함한 전 장병은 부대 근처에서 생활하게 됩니다. 운동과 같은 취미 생활을 가지고 있지만, 함께할 사람을 쉽게 구하지 못하는 상황을 많이 목격했습니다. ToMAS의 인원모집 기능은 근처 부대 단위로 간편하게 인원을 모집할 수 있어서 운동과 같은 일회성 인원모집을 하는데에 제격입니다. 같은 영내 부대라도 연락할 수단이 없어서 함께 운동이나 동아리 활동을 하지 못했는데, 이 기능을 통해 인원모집의 어려움을 없애고 큰 단위의 친목을 도모할 수 있습니다. 인원 모집의 편의성 때문에 운동활동이 더욱 활발하게 일어날 것이고 이를 통해 체력을 증진할 수 있습니다. 체력증진은 곧 전투력향상으로 이어져 더 강한 국방을 만들 수 있습니다.
국방부에서 진행하는 공모전이나 대회가 상당히 많음에도 불구하고, 함께할 팀원을 구하기 힘들어서 대회에 참가하지 못하는 경우가 있습니다. 각자 자신 있는 역할을 분배하여 팀원을 모집할 수 있는 기능을 제공한다면 군 내에서 진행하는 대회의 참가율을 높임과 동시에 대회 완성작의 퀄리티도 높일 수 있을 것이라 기대합니다.

특징
### 1. 부대 단위로 데이터베이스 분리
<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">
플리마켓과 마찬가지로 이동 가능한 영내 단위에서 인원 모집이 가능하게 합니다. 이를 통해 인원모집의 편의성을 높이고, 검색을 용이하게 합니다.


<details>

<summary>기술 자세히</summary>

<div markdown="1">

![group_firebase](https://app.diagrams.net/#G1dr_t4NB-hfUMj0nYMB7nSDoSa1YI4Lpg)

마찬가지로 RecylcerTreeView를 사용해 소속 부대를 변경할 수 있게 했습니다.
자세한 사항 [링크 걸기]

---

</div>

</details>

---

</div>

</details>


### 2.  참가 신청/취소 기능을 제공

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

[sliding layout 올라온 것 사진 추가]



<details>
<summary>기술 자세히</summary>

<div markdown="1">

`SlidingUpPanelLayout` 라이브러리를 이용해 구현했습니다.

```java
btn_enroll.setOnClickListener(new Button.OnClickListener() {  
    @Override  
  public void onClick(View view) {  
        // user ID를 통해 검색하고 list에 있으면 없애고, list에 없으면 추가하기)  
  String mUid = mainActivity.getUid();  
 if (tmp_participants.containsKey(mainActivity.getUid()))  
        { // 이미 참가자에 uid가 있는 경우 : array에서 삭제  
  mPostReference.update("participants."+mUid , FieldValue.delete());  
  mPostReference.update("now_people", FieldValue.increment(-1));  
  // fragment 새로고침  
  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  
  fragmentTransaction.detach(GroupContent.this).attach(GroupContent.this).commit();  
  }  
        else {  
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)  
            {  
                Map<String, String> my_info = new HashMap<>();  
  my_info.put("name", mainActivity.preferences.getString("이름", ""));  
  my_info.put("phonenumber", mainActivity.preferences.getString("phonenumber", ""));  
  my_info.put("position", position_edit.getText().toString());  
  mPostReference.update("participants." + mUid, my_info);  
  mPostReference.update("now_people", FieldValue.increment(1));  
  
  // fragment 새로고침  
  FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  
  fragmentTransaction.detach(GroupContent.this).attach(GroupContent.this).commit();  
  }  
            else  
  {  
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);  
  }  
        }  
    }  
});

```

새로운 참가자가 참가 신청 버튼을 누르면 SlidingLayout이 위로 올라오는 건 `setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED)`로 구현했습니다. 

![group_firebase](https://app.diagrams.net/#G1dr_t4NB-hfUMj0nYMB7nSDoSa1YI4Lpg)

![group_paricipants_example](https://github.com/rlarla915/readme_sample/blob/main/group_participants_example.PNG)
 `update()`함수와 Map field 명 뒤에 "."을 붙이는 방법을 사용해서 Map에 새로운 Map을 추가합니다. `name`, `phonenumber`, `position` key 세 개를 채운 Map을 value로 유저의 ID를 key로 `participants`에 추가합니다.

`participants`에 유저 ID가 있는지 확인하고 있으면 참가 신청 버튼을 취소 버튼으로 바꿉니다. 참가 취소 버튼을 누르면 participants에 있는 유저 ID 키와 value를 `Field.delete()`로 지웁니다.

사용 라이브러리 : https://github.com/umano/AndroidSlidingUpPanel
```gradle
implementation 'com.sothree.slidinguppanel:library:3.4.0'

```

---

</div>

</details>

---

  
</div>

</details>

### 3. 참가 신청/취소 기능을 제공

<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

참가 신청을 


  <details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">


---
</div>

</details>

---
</div>


</details>



### 4. 카테고리 별로 나눠서 제공

  

  


## 4) 일정표

  

근무,휴가,훈련 등의 개인적인 일정을 달력에 기록하는 기능을 제공합니다. 소속부대의 중간관리자가 부대 훈련 일정을 추가하면 부대원들의 개인일정표에 자동으로 업데이트 하는 기능을 제공합니다.

  

## 5) 설문조사

군 특성 상 ToMAS의 설문조사는 Google Docs에 비해 장점이 많습니다.
특징 | ToMAS | Google Docs
---------|----------|---------
익명으로 처리되는가?| O|O
익명이면서 누가 참가하지 않았는지 알 수 있는가? | O|X
부대원에게 전파하기가 쉬운가? |O|X
중복된 설문조사를 방지할 수 있는가? | O | X
제출된 설문조사를 수정할 수 있는가? |O|X
다양한 질문 형태가 있는가? |X|O

하지만 ToMAS의 설문조사도 추후 업데이트를 통해 더 다양한 질문 형태를 제공할 수 있습니다.


특징
### 1. 관리자가 객관식과 주관식 두 가지 분류로 설문조사를 등록할 수 있다.
<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">
[객관식 주관식 사진 넣기]
오른쪽 아래의 +버튼을 눌러 질문을 추가할 수 있습니다. 질문 유형은 객관식과 주관식 두 가지 입니다.
설문조사 질문은 오른쪽의 X버튼을 통해 자유롭게 삭제할 수 있습니다.

객관식
먼저 질문을 등록할 수 있는 EditText가 한 줄 제공됩니다.
객관식 보기 추가 버튼을 눌러 객관식 선택지를 추가할 수 있고, 여기에 선택지 내용을 적을 수 있는 EditText가 제공됩니다.
보기 오른쪽에 X버튼을 통해 자유롭게 객관식 선택지를 삭제할 수 있습니다.

주관식
주관식 질문을 등록할 수 있는 EditText가 한 줄 제공됩니다.
아래에 대답을 기입할 수 있는 EditText가 한 줄 제공되지만, 설문조사 등록 시에는 기입을 하는 용도가 아니고, 어떤 식으로 구성되는지 보여주는 용도입니다.

설문조사 등록 버튼을 누르면 설문조사 질문을 하나하나 종합하여 firestore에 저장합니다.


<details>

<summary>기술 자세히</summary>

<div markdown="1">

```java
radioButton1.setOnClickListener(new OnClickListener() {  
    @Override  
  public void onClick(View view) {  
        container.removeAllViews();  
  Button add_button = new Button(tmp_context);  
  add_button.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));  
  add_button.setText("객관식 보기 추가");  
  add_button.setOnClickListener(new OnClickListener() {  
            @Override  
  public void onClick(View view) {  
                MultipleChoiceSelectionCustomView tmp_selection_customView = new MultipleChoiceSelectionCustomView(tmp_context);  
  tmp_selection_customView.delete_button.setOnClickListener(new OnClickListener() {  
                    @Override  
  public void onClick(View view) {  
                        container.removeView(tmp_selection_customView);  
  multi_chice_selection_list.remove(tmp_selection_customView.multiple_choice_index);  
  recount();  
  }  
                });  
  container.addView(tmp_selection_customView, multi_chice_selection_list.size());  
  multi_chice_selection_list.add(tmp_selection_customView);  
  recount();  
  }  
        });  
  container.addView(add_button);  
  }  
});  
radioButton2.setOnClickListener(new OnClickListener() {  
    @Override  
  public void onClick(View view) {  
        container.removeAllViews();  
  EditText tmp_edit = new EditText(tmp_context);  
  tmp_edit.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));  
  container.addView(tmp_edit);  
  }  
});
```

customView를 통해 문항 추가 기능을 구현했습니다. 오른쪽 아래의 +버튼을 눌러 문항을 추가하면, `SurveyQuestionCustomView.java` 을 `RegisterSocialSurvey.java`에 넣어줍니다. RadioButton로 객관식 주관식을 선택할 수 있고, 각각의 경우 기존의 container에 있던 내용을 지우고 새로 형식을 구성합니다. 
 
---

</div>

</details>

---
  
</div>

</details>


### 2. 관리자가 개개인의 설문조사 결과와 종합된 결과를 확인할 수 있다.


<details>

<summary>접기/펼치기 버튼</summary>

<div markdown="1">

[관리자로 SocialSurvey 들어간 것 사진 추가]
전체 설문조사 결과보기와 지금까지 참여한 부대원들의 설문조사 결과가 user1, user2와 같은 익명으로 처리되어 리스트되어 나와있습니다.

[개인 결과 보기 사진 추가]
개인 결과 보기를 하면 그 사람이 기록한 결과가 객관식에는 RadioButton이 클릭된 형태로, 주관식에는 EditText가 채워진 형태로 제공됩니다. 이 때 RadioButton과 EditText는 수정이 불가능하게 disable되어 있습니다.

[전체 결과 보기 사진 추가]
전체 결과 보기를 하면 객관식의 경우 선택지 오른쪽에 선택한 인원의 숫자가 나타나고, 주관식의 경우 각각의 대답이 기록되어 제공됩니다.



<details>

<summary>기술 자세히</summary>


<div markdown="1">

마찬가지로 customView를 이용해 표현했습니다.

[firestore 사진 추가]
불러오는 정보의 데이터 구조는 다음과 같습니다.

![firestore 구조](https://github.com/rlarla915/readme_sample/blob/main/survey_firestore.png)

개인 설문 조사 결과
`SurveyContentResultIndividual.java`

```java
mPostReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {  
    @Override  
  public void onComplete(@NonNull Task<DocumentSnapshot> task) {  
        if (task.isSuccessful()) {  DocumentSnapshot document = task.getResult();  
 if (document.exists()) { title_textView.setText(document.get("title", String.class));  
  due_date_textView.setText(document.get("due_date", String.class));  
  writer_textView.setText(document.get("writer", String.class));  
  mPostReference.collection("submissions").document(participant_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {  
                    @Override  
  public void onComplete(@NonNull Task<DocumentSnapshot> task) {  
                        if (task.isSuccessful()) {   DocumentSnapshot adocument = task.getResult();  
 if (adocument.exists()) {  answers_list = (ArrayList<String>) adocument.get("answers");    mPostReference.collection("questions").orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {  
                                    @Override  
  public void onComplete(@NonNull Task<QuerySnapshot> task) {  
                                        if (task.isSuccessful()) {  
                                            int count = 1;  
 for (QueryDocumentSnapshot tmp_document : task.getResult()) {  
                          int tmp_type = tmp_document.get("type", Integer.class);  
  String item_question = tmp_document.get("question", String.class);  
  SurveyContentResultCustomView tmp_customView;  
 if (tmp_type == 1) {  
  tmp_customView = new SurveyContentResultCustomView(mainActivity, null, tmp_type, count, item_question, (ArrayList<String>) tmp_document.get("multi_choice_questions"), answers_list.get(count-1));  
  } else {  
  tmp_customView = new SurveyContentResultCustomView(mainActivity, null, tmp_type, count, item_question, null, answers_list.get(count-1));  
  }    container_linearLayout.addView(tmp_customView, count + 1);  
  count++;  
  }  
                                        }  
                                    }  
                                });  
  }  
                        }  
                    }  
                });  
  }  
        }  
    }  
});
```

`SurveryContentResultTotal.java` 참고

`submissions` collection에서 개인 사용자의 ID로 document를 얻어냅니다. document 내의 `answers` field에는 순서대로 index에 대한 개인 사용자의 설문조사 제출 결과가 들어있습니다.  이걸 먼저 받아온 뒤, `question` collection에서 받아온 질문과 결합해 `SurveyContentResultCustomView`로 넘겨줍니다.
`SurveyContentResultCustomView`에서는
`questions` collection에서 받아온 document들을 `index`로 정렬하고 `type`에 따라 항목을 구성한 뒤, `type`이 1인 경우(객관식) `multi_choice_questions`를 가지고 객관식 선택지를 만듭니다. 
type이 2인 경우(주관식)
`question`만 받아와 TextView에 넣어줍니다.

전체 설문 조사 결과
개인 설문 조사 결과와 유사하게 구성하나, `submissions` collection에서 모든 document를 for 문을 통해 접근한뒤, 결과를 종합해서 보여준다는 차이점이 있습니다. 전체 설문 조사 결과에서는 RadioButton을 설정하지 않고, submissions에서 가져온 결과를 넣어줍니다.


---

</div>

</details>

---


</div>

</details>


### 3. 개인이 제출한 설문조사는 계속 수정 제출이 가능하다.
### 4. 개인이 설문조사에 참가했는지 쉽게 확인할 수 있다.
### 5. 기본적으로 익명이 보장되지만 실명으로도 할 수 있다.
  


## 6) 소속부대 공지사항
 

중대와 대대 단위로 설문조사 기능과 공지사항 기능을 제공합니다. 소속부대의 중간관리자가 설문조사와 공지사항을 등록 할 수 있게 제공되어 있습니다. 필수 공지사항 같은 경우는 부대원들이 확인을 했는지 안했는지를 표현해주는 기능을 만들어서 중간관리자가 부대원들의 내용 확인 여부를 체크 할 수 있게합니다.






## 컴퓨터 구성 / 필수 조건 안내 (Prerequisites)
* ECMAScript 6 지원 브라우저 사용
* 권장: Google Chrome 버젼 77 이상

## 기술 스택 (Technique Used) (예시)
### Server(back-end)
 -  nodejs, php, java 등 서버 언어 버전 
 - express, laravel, sptring boot 등 사용한 프레임워크 
 - DB 등 사용한 다른 프로그램 
 
### front-end
 -  react.js, vue.js 등 사용한 front-end 프레임워크 
 -  UI framework
 - 기타 사용한 라이브러리

## 설치 안내 (Installation Process)
```bash
$ git clone git주소
$ yarn or npm install
$ yarn start or npm run start
```

## 프로젝트 사용법 (Getting Started)
**마크다운 문법을 이용하여 자유롭게 기재**

잘 모를 경우
구글 검색 - 마크다운 문법
[https://post.naver.com/viewer/postView.nhn?volumeNo=24627214&memberNo=42458017](https://post.naver.com/viewer/postView.nhn?volumeNo=24627214&memberNo=42458017)

 편한 마크다운 에디터를 찾아서 사용
 샘플 에디터 [https://stackedit.io/app#](https://stackedit.io/app#)
 
## 팀 정보 (Team Information)
- 김경민 (rlarla915@naver.com), Github Id: rlarla915
- 김도한 (oneblue14@gmail.com), Github Id: commitcomplete

## 저작권 및 사용권 정보 (Copyleft / End User License)
 * [MIT](https://github.com/osam2020-WEB/Sample-ProjectName-TeamName/blob/master/license.md)
 - TextView에서 이미지 포함한 html 띄우기 : https://stackoverflow.com/questions/16179285/html-imagegetter-textview/16209680#16209680
 - 아래에서 올라오는 layout : https://github.com/umano/AndroidSlidingUpPanel
 - 로그인 ui 구현 참고 : https://github.com/Shashank02051997/LoginUI-Android
 - 안드로이드 갤러리에서 비트맵형식으로 이미지 가져오기 :https://lktprogrammer.tistory.com/188
 - 로딩화면 구현 : https://andro-jinu.tistory.com/entry/androidstudio2
 
