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

package com.commandModel.orderToDb;

import android.content.Context;

import com.dbModel.dao.InstructionRequestDao;
import com.dbModel.entity.InstructionRequest;
import com.sgs.jfei.common.AppContext;

public class InstructionRequestManager {
    private final String TAG = "DataManager";

    private Context context;

    private InstructionRequestManager(Context context) {
        this.context = context;
    }

    private static InstructionRequestManager instance;

    public static InstructionRequestManager getInstance() {
        if (instance == null) {
            synchronized (InstructionRequestManager.class) {
                if (instance == null) {
                    instance = new InstructionRequestManager(AppContext.getInstance());
                }
            }
        }
        return instance;
    }

    /**
     * 保存当前用户,只在登录或注销时调用
     *
     * @param user user == null >> user = new User();
     */
    public void saveInstructionRequest(InstructionRequest user) {
        new InstructionRequestDao(context).add(user);
    }
}
