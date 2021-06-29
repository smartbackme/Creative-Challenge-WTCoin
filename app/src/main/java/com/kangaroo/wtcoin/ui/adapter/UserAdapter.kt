package com.kangaroo.wtcoin.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.data.model.User
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-02-05 10:14
 * #测试#
 */
class UserAdapter : BaseQuickAdapter<User, BaseViewHolder>(R.layout.item_user), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: User) {
        holder.itemView.u.text = item.name
    }
}
