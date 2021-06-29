package com.kangaroo.wtcoin.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kangaroo.wtcoin.R
import com.kangaroo.wtcoin.data.model.CoinNodeCount
import kotlinx.android.synthetic.main.item_coin.view.*

/**
 * 自动生成：by WaTaNaBe on 2021-02-05 10:14
 * #测试#
 */
class DataAdapter : BaseQuickAdapter<CoinNodeCount, BaseViewHolder>(R.layout.item_coin), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: CoinNodeCount) {
        holder.itemView.ids.text = "空气币编号："+item.ids
        holder.itemView.user.text = "创建用户："+item.user.name
        holder.itemView.count.text = "拥有数量："+item.count.toString()
    }
}
