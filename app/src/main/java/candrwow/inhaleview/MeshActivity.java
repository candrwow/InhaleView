package candrwow.inhaleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import candrwow.inhaleview.Listener.CoverListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MeshActivity extends AppCompatActivity implements CoverListener {

    @BindView(R.id.activity_mesh)
    RelativeLayout activityMesh;
    @BindView(R.id.rv_inhale)
    RecyclerView rvInhale;
    @BindView(R.id.mv_inhale)
    candrwow.inhaleview.CustomView.MeshView mvInhale;
    MeshViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesh);
        ButterKnife.bind(this);
        rvInhale.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MeshViewAdapter(this);
        rvInhale.setAdapter(adapter);
        rvInhale.setItemAnimator(new DefaultItemAnimator());

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //锚点的坐标是相对于meshView的左上角
                        mvInhale.startAnim(15, 700, 1360);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void onUpdate(float value) {

    }


}
