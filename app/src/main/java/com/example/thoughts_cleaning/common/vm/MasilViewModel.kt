package com.example.thoughts_cleaning.common.vm

import androidx.lifecycle.LiveData
import com.example.thoughts_cleaning.api.Prefs
import com.example.thoughts_cleaning.api.ServerNetworkApi
import com.example.thoughts_cleaning.api.ServerNetworkClient
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.base.MoveEvent
import kotlin.text.isEmpty

/**
 * Created by SeoKang on 2022-09-14.
 *
 * https://bb-library.tistory.com/264
 * https://zladnrms.tistory.com/146
 */

abstract class MasilViewModel: BaseViewModel(), ApiErrorHandler {
    //api 서버 연결하는 부분
    protected val api: ServerNetworkApi = ServerNetworkClient.getClient()
//    protected val suniApi: SuniApi = SuniApiClient.getClient()


    val _moveEvent = SingleLiveEvent<MoveEvent>()
    val moveEvent: LiveData<MoveEvent> get() = _moveEvent

    protected suspend fun<T : ResBase> callApi(black: suspend () -> T) : T {
        if (Prefs.accessToken?.isEmpty() == true) throw SuniException.InvalidSessionException("Invalid Session") as Throwable
        if (Prefs.userId == 0) throw SuniException.InvalidSessionException("Invalid Session") as Throwable

        return checkResult(black())
    }

//    protected suspend fun<T : ResBase> callNotNeedIdApi(black: suspend () -> T) : T {
//        if (Prefs.sessionKey.isEmpty()) throw InvalidSessionException("Invalid Session")
//
//        return checkResult(black())
//    }

//    protected suspend fun<T : ResBase> callNotNeedSessionIdApi(black: suspend () -> T) : T = checkResult(black())

}