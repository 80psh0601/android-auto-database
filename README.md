# android-ggomdol-autodatabase

The Auto Database on Android Platform 

You can create Android Database easily. Java Reflect was used for the library.  

1. EntityOne.java : Create Entity Class matching columns of database tables.
---------------------------------------------------

public class EntityOne extends EntityAbstract{
	
	public String one;
	public int two;
	public long three;
}

2. TableCreator.java : Register the entity.
-------------------------------------------
	public static List<String> getCreateTableDDL() {

		ArrayList<String> strDllArrayList = new ArrayList<String>();
		strDllArrayList.add(createTableString(EntityOne.class));
		strDllArrayList.add(createTableString(EntityTwo.class));

		return strDllArrayList;		
	}

3. MainActivity.java : Done, you can use it right away.
----------------------

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


Copyright (c) 2016 SangHyun Park

Licensed under the GNU Lesser General Public License v3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

https://www.gnu.org/licenses/lgpl-3.0.en.html

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
