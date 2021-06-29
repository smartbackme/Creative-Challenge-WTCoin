package com.kangaroo.wtcoin.tools

import com.kangaroo.wtcoin.data.model.*
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.json.HJson
import com.kangraoo.basektlib.tools.store.MMKVStore
import com.kangraoo.basektlib.tools.store.MemoryStore

/**
 * @author shidawei
 * 创建日期：2021/6/21
 * 描述：
 */
const val USER:String = "user"
const val WTCOIN:String = "wtcoin"
const val USERS:String = "users"
const val LIAN:String = "lian"

object UStore {

    fun putUser(user : UserModel){
        MMKVStore.instance(WTCOIN).put(USER,user)
    }

    fun getUser():UserModel? = MMKVStore.instance(WTCOIN).get(USER,null,UserModel::class.java)

    fun clearUser(){
        MMKVStore.instance(WTCOIN).remove(USER)
    }

    fun putUserList(user : UserList){
        MMKVStore.instance(WTCOIN).put(USERS,user)
    }

    fun getUserList():UserList? = MMKVStore.instance(WTCOIN).get(USERS,null,UserList::class.java)

    fun getLian():CoinNode? = MMKVStore.instance(WTCOIN).get(LIAN,null,CoinNode::class.java)

    fun putLocLian(coin : CoinNode){
        MMKVStore.instance(WTCOIN).put(LIAN,coin)
    }



    var set:HashSet<UserList> = HashSet<UserList>()

    @Synchronized
    fun putUserListAll(user : UserList){
        user.username = null
        if(set.size>1000){
            return
        }
        set.add(user)
    }

    @Synchronized
    fun getUserListFromQt():UserList{
        var userList = HashSet<User>()
        var pre = getUserList()?.user
        if(pre!=null){
            userList.addAll(pre)
        }
        var iter = set.iterator()
        while (iter.hasNext()){
            userList.addAll(iter.next().user)
            iter.remove()
        }
        var j = UserList(null,null,userList)
        putUserList(j)
        return j
    }

    @Synchronized
    fun putLian(lian: CoinNodeModel) {
        val prelian = getLian()
        if(prelian!=null){
            bianliLian()
            if(lian.lianchang> length){
                putLocLian(lian.coinNode)
                bianliLian()
            }else if (lian.lianchang == length&&lian.lastTime < lastNode!!.time){
                putLocLian(lian.coinNode)
                bianliLian()
            }else{
                MqttUtil.message(MqttUtil.un, HJson.toJson(CoinNodeModel(extusername = getUser()!!.name,lianchang = length,lastTime = lastNode!!.time,coinNode = node!!)))
            }
        }else{
            putLocLian(lian.coinNode)
            bianliLian()
        }
    }

    @Volatile
    var lastNode:CoinNode? = null

    @Volatile
    var node:CoinNode? = null

    @Volatile
    var length = 0

    @Synchronized
    fun bianliLian(){
        val prelian = getLian()
        node = prelian
        var temp = prelian
        if(temp==null){
            length = 0
            lastNode = null
            return
        }
        var l = 1
        while (temp!!.next!=null){
            l++
            temp = temp.next
        }
        length = l
        lastNode = temp
    }


    @Synchronized
    fun putOne(last:CoinNode?,length :Int = 0,nodes:CoinNode? = null){
        var node = CoinNode(last?.hashCode()?.toString(),
            HString.to32UUID(),
            UTime.currentTimeMillis(), User(getUser()?.name!!),null)

        if(last!=null){
            last.next = node
            MqttUtil.message(MqttUtil.un, HJson.toJson(CoinNodeModel(extusername = getUser()!!.name,lianchang = 1 + length,lastTime = node.time,coinNode = nodes!!)))
            node = nodes
        }else{
            MqttUtil.message(MqttUtil.un, HJson.toJson(CoinNodeModel(extusername = getUser()!!.name,lianchang = 1,lastTime = node.time,coinNode = node)))
        }
        putLocLian(node)
        bianliLian()
    }

}