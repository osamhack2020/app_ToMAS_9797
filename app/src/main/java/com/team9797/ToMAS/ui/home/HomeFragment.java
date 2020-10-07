package com.team9797.ToMAS.ui.home;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.team9797.ToMAS.MainActivity;
import com.team9797.ToMAS.R;
import com.team9797.ToMAS.fragment_template;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    MainActivity mainActivity;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity)getActivity();
        fragmentManager = getFragmentManager();
        btn1 = (Button) root.findViewById(R.id.home_btn1);
        btn2 = (Button) root.findViewById(R.id.home_btn2);
        btn3 = (Button) root.findViewById(R.id.home_btn3);
        btn4 = (Button) root.findViewById(R.id.home_btn4);
        btn1.setOnClickListener(btn_home);
        btn2.setOnClickListener(btn_home);
        btn3.setOnClickListener(btn_home);
        btn4.setOnClickListener(btn_home);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.push_title("홈");
        mainActivity.set_title();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    View.OnClickListener btn_home = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment change_fragment = null;
            fragmentTransaction = fragmentManager.beginTransaction();
            String title = "";
            final ArrayList<String> child_list = new ArrayList<>();
            switch (view.getId()) {
                case R.id.home_btn1:
                    /*
                    * firestore_child_list_listener에 mainactivity, fragmentmanager, fragment_style, title, parents순으로 넣으면
                    * firestore_listener에서 child_list를 불러오고 이를 fragment_template에 넘긴다.
                    * 이때 넘기는 parents는 자기자신을 포함한 path이다. 네이밍이 잘못됨.
                    */
                    title = "자기개발";
                    change_fragment = new fragment_template();
                    Bundle args = new Bundle();
                    args.putInt("fragment_style", 1);
                    args.putString("title", title);
                    args.putString("path", "mainpage");
                    change_fragment.setArguments(args);
                    break;
                    /* 살려야함 이건
                case R.id.home_btn2:
                    change_fragment = new boardFragment();
                    title = "게시판";
                    break;
                case R.id.home_btn3:
                    change_fragment = new marketFragment();
                    title = "플리마켓";
                    break;
                case R.id.home_btn4:
                    change_fragment = new groupFragment();
                    title = "인원모집";
                    break;

                     */
            }
            mainActivity.push_title(title);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.nav_host_fragment, change_fragment).commit();
        }
    };
}