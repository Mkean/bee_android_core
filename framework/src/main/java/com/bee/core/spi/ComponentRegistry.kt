package com.bee.core.spi

import android.text.TextUtils
import com.bee.core.delegate.IComponentDescription
import com.bee.core.utils.ServiceLoaderUtil

/**
 *@Description:
 */
class ComponentRegistry {

    companion object {

        private const val UN_KNOWN = "un_known"

        private val mComponents: HashMap<String, ArrayList<IComponentDescription>> = HashMap()

        fun loadComponents() {

            mComponents.clear()

            ServiceLoaderUtil.loadAllServices(IComponentDescription::class.java).also { it ->
                it.forEach {
                    if (it != null) {
                        val group = if (TextUtils.isEmpty(it.group)) {
                            UN_KNOWN
                        } else {
                            it.group
                        }

                        if (mComponents[group] == null) {
                            mComponents[group] = ArrayList()
                        }

                        mComponents[group]!!.add(it)
                    }
                }
            }
        }

        /**
         *根据分组获取其子组件
         */
        fun getComponents(group: String): List<IComponentDescription> {
            return if (TextUtils.isEmpty(group)) {
                mComponents[UN_KNOWN] as List<IComponentDescription>
            } else {
                mComponents[group] as List<IComponentDescription>
            }
        }

        /**
         * 获取所有组件
         */
        fun getAllComponents(): List<IComponentDescription> {
            val list: ArrayList<IComponentDescription> = ArrayList()
            mComponents.forEach {
                list.addAll(it.value)
            }
            return list
        }

    }
}