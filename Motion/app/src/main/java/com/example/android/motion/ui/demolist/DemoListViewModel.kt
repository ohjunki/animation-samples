/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.motion.ui.demolist

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.motion.model.Demo

class DemoListViewModel(application: Application) : AndroidViewModel(application) {

    /***
     * 이중으로 LiveData를 감싸서 외부에서는 demos의 데이터를 수정하지 못하도록 막는다.
     */
    private val _demos = MutableLiveData<List<Demo>>()
    val demos: LiveData<List<Demo>> = _demos

    init {
        val packageManager = getApplication<Application>().packageManager
        val resolveInfoList = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).addCategory(Demo.CATEGORY),
            PackageManager.GET_META_DATA
        )

        /**
         * Manifest에 Activity를 정의할때 Category를 지정할 수 있고 아래는 특정 카테고리의 activity 리스트를 받아온다.
         * 리스트를 받아서 Intent를 생성할 수 있는 Demo Class로 매핑을 해준다.
         * 추가 메타데이터로 Description, Apis를 생성하여 표시하고 있다.

        <activity
            android:name=".demo.reorder.ReorderActivity"
            android:label="@string/reorder_label">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="com.example.android.motion.intent.category.DEMO" />
            </intent-filter>

            <meta-data
                android:name="com.example.android.motion.demo.DESCRIPTION"
                android:value="@string/reorder_description" />
            <meta-data
                android:name="com.example.android.motion.demo.APIS"
                android:resource="@array/reorder_apis" />
        </activity>

         */
        val resources = application.resources
        _demos.value = resolveInfoList.map { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo
            val metaData = activityInfo.metaData
            val apisId = metaData?.getInt(Demo.META_DATA_APIS, 0) ?: 0
            Demo(
                activityInfo.applicationInfo.packageName,
                activityInfo.name,
                activityInfo.loadLabel(packageManager).toString(),
                metaData?.getString(Demo.META_DATA_DESCRIPTION),
                if (apisId == 0) emptyList() else resources.getStringArray(apisId).toList()
            )
        }
    }
}
