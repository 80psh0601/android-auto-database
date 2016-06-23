package ggomdol.autodatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ggomdol.autodatabase.dao.DaoBase;
import ggomdol.autodatabase.entity.EntityOne;
import ggomdol.autodatabase.entity.EntityTwo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();
    }

    private void initWidget() {

        try {
            //--------- insert ----------
            EntityOne entity = new EntityOne();
            entity.one = "one";
            entity.two = 2;
            entity.three = 3L;

            DaoBase.getInstance(EntityOne.class).insertEntity(this, entity);

            EntityTwo entity2 = new EntityTwo();
            entity2.four = "four";
            entity2.five = 6;
            entity2.six = 6L;

            DaoBase.getInstance(EntityTwo.class).insertEntity(this, entity2);

            //--------- select ------------
            ArrayList<Object> array = DaoBase.getInstance(EntityOne.class).getEntityList(this);
            for (Object object : array) {
                entity = (EntityOne) object;
            }

            ArrayList<Object> array2 = DaoBase.getInstance(EntityTwo.class).getEntityList(this);
            for (Object object : array2) {
                entity2 = (EntityTwo) object;
            }

            //-------- Delete --------------
            for (Object object : array) {
                entity = (EntityOne) object;
                DaoBase.getInstance(EntityOne.class).deleteData(this, entity.ROWID);
            }

            for (Object object : array2) {
                entity2 = (EntityTwo) object;
                DaoBase.getInstance(EntityTwo.class).deleteData(this, entity.ROWID);
            }

        } catch (Exception e) {}
    }
}
