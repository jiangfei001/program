/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.sgs.businessmodule.taskModel.commandModel.orderToDb;

import android.content.Context;

import com.sgs.AppContext;
import com.sgs.middle.dbModel.dao.OrderDao;
import com.sgs.middle.dbModel.entity.Order;

public class OrderManager {
    private final String TAG = "DataManager";

    private Context context;

    private OrderManager(Context context) {
        this.context = context;
    }

    private static OrderManager instance;

    public static OrderManager getInstance() {
        if (instance == null) {
            synchronized (OrderManager.class) {
                if (instance == null) {
                    instance = new OrderManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    //用户 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private String PATH_USER = "PATH_USER";

    public final String KEY_USER = "KEY_USER";
    public final String KEY_USER_ID = "KEY_USER_ID";
    public final String KEY_USER_NAME = "KEY_USER_NAME";
    public final String KEY_USER_PHONE = "KEY_USER_PHONE";

    public final String KEY_CURRENT_USER_ID = "KEY_CURRENT_USER_ID";
    public final String KEY_LAST_USER_ID = "KEY_LAST_USER_ID";


    /**
     * 保存当前用户,只在登录或注销时调用
     *
     * @param user    user == null >> user = new User();
     */
    public void saveOrder(Order user) {
        Order u = new Order();
        new OrderDao(context).add(u);
    }
}
