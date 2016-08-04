package com.beyondeye.reduks.modules

import com.beyondeye.reduks.*

class MultiReduks2<S1:Any,S2:Any>(def1: ReduksModule.Def<S1>,
                                  def2: ReduksModule.Def<S2>) : MultiReduks(),Reduks<MultiState2<S1,S2>>{
    override val ctx: ReduksContext=def1.ctx+def2.ctx
    val r1= ReduksModule<S1>(def1)
    val r2= ReduksModule<S2>(def2)
    override val rmap= mapOf(def1.ctx to r1,def2.ctx to r2)
    override fun dispatchActionWithContext(a: ActionWithContext): Any = when (a.context) {
            r1.ctx -> r1.dispatch(a.action)
            r2.ctx -> r2.dispatch(a.action)
            else -> throw IllegalArgumentException("no registered module with id ${a.context.moduleId}")
        }
    override val store:Store<MultiState2<S1, S2>> = object:Store<MultiState2<S1, S2>>,MultiStore2<S1,S2>(r1.ctx,r1.store,r2.ctx,r2.store) {
        override val state: MultiState2<S1, S2> get()= MultiState2(r1.store.state,r2.store.state)
        override var dispatch=dispatchWrappedAction
        override fun subscribe(storeSubscriber: StoreSubscriber<MultiState2<S1, S2>>): StoreSubscription {
            val s1=store1.subscribe(StoreSubscriber { storeSubscriber.onStateChange(state) })
            val s2=store2.subscribe(StoreSubscriber { storeSubscriber.onStateChange(state) })
            return MultiStoreSubscription(s1, s2)
        }
    }
    fun subscribe(storeSubscriber: StoreSubscriber<MultiState2<S1, S2>>): StoreSubscription =store.subscribe(storeSubscriber)
    /* empty subscriber: if you want to add a subscriber on global state changes, call [subscribe] function above */
    override val storeSubscriber = StoreSubscriber<MultiState2<S1, S2>>{}
    /* empty subscription: if you want to add a  susbscriber on global state changes, call [subscribe] function above */
    override val storeSubscription= StoreSubscription {}
    init {
//        r1.store.applyMiddleware(UnwrapActionMiddleware())
//        r2.store.applyMiddleware(UnwrapActionMiddleware())
        r1.dispatch(def1.startAction)
        r2.dispatch(def2.startAction)
    }
}

