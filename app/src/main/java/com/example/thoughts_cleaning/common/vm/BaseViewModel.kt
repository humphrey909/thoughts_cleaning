package com.example.thoughts_cleaning.common.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts_cleaning.api.model.ResBase
import com.example.thoughts_cleaning.common.Constants
import com.example.thoughts_cleaning.common.ErrorCodeApp
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Created by SeoKang on 2022-09-14.
 *
 * https://bb-library.tistory.com/264
 * https://zladnrms.tistory.com/146
 */

abstract class BaseViewModel : ViewModel(), ApiErrorHandler {

    companion object {
        const val TAG = "BaseViewModel"
    }

    protected val _showAppEvent = SingleLiveEvent<ErrorCodeApp?>()
    val showAppEvent: LiveData<ErrorCodeApp?> get() = _showAppEvent

    protected val _showServerEvent = SingleLiveEvent<Exception>()
    val showServerEvent: LiveData<Exception> get() = _showServerEvent

    open val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()

        when (throwable) {
            is SuniException.GeneralException -> exceptionGeneral(_showServerEvent, throwable)
            is SuniException.InvalidSessionException -> exceptionInvalidSession(_showServerEvent, throwable)
            is SuniException.InvalidLanguageException -> exceptionInvalidLanguage(_showServerEvent, throwable)
            is SuniException.ExpiredSessionException -> exceptionExpiredSession(_showServerEvent, throwable)
            is SuniException.NeedAppUpdateException -> exceptionNeedAppUpdate(_showServerEvent, throwable)
            is SuniException.NoVtsException -> exceptionNoVts(_showServerEvent, throwable)
            is SuniException.ShopAlreadyOrderException -> exceptionShopAlreadyOrder(_showServerEvent, throwable)
            is SuniException.MustLoginAsOwnerException -> exceptionMustLoginAsOwner(_showServerEvent, throwable)
            is SuniException.EventNotEndException -> exceptionEventNotEnd(_showServerEvent, throwable)
            is SuniException.EventIsTooOldException -> exceptionEventIsTooOld(_showServerEvent, throwable)
            is SuniException.FamilyAlreadyException -> exceptionFamilyAlready(_showServerEvent, throwable)
            is SuniException.FollowAlreadyException -> exceptionFollowAlready(_showServerEvent, throwable)
            is SuniException.FollowExceedSearchException -> exceptionFollowExceedSearch(_showServerEvent, throwable)
            is SuniException.NanumAlreadyGiveRequestException -> exceptionNanumAlreadyGiveRequest(_showServerEvent, throwable)
            is SuniException.NanumAlreadyBuyRequestException -> exceptionNanumAlreadyBuyRequest(_showServerEvent, throwable)
            is SuniException.NoOnAirBroadcastException -> exceptionNoOnAirBroadcast(_showServerEvent, throwable)
            else -> exceptionNetwork(_showAppEvent)
        }


        Log.e(TAG, "CoroutineExceptionHandler $throwable")
    }

    protected fun<T : ResBase> checkResult(data: T): T {
        if (data.result == Constants.KEY_OK) {
            return data
        }else {
            val exception = when (data.errorCode) {
                Constants.ERROR_CODE_NO_VTS -> SuniException.NoVtsException(data.errorMessage)
                Constants.ERROR_CODE_INVALID_LANGUAGE -> SuniException.InvalidLanguageException(data.errorMessage)
                Constants.ERROR_CODE_SWER_005 -> SuniException.InvalidSessionException(data.errorMessage)
                Constants.ERROR_CODE_SWER_004 -> SuniException.ExpiredSessionException(data.errorMessage)
                Constants.ERROR_CODE_SHOP_ALREADY_ORDER -> SuniException.ShopAlreadyOrderException(
                    data.errorMessage
                )
                Constants.ERROR_CODE_NANUM_ALREADY_GIVE_REQUEST -> SuniException.NanumAlreadyGiveRequestException(
                    data.errorMessage
                )
                Constants.ERROR_CODE_NANUM_ALREADY_BUY_REQUEST -> SuniException.NanumAlreadyBuyRequestException(
                    data.errorMessage
                )
                Constants.ERROR_CODE_MUST_LOGIN_AS_OWNER -> SuniException.MustLoginAsOwnerException(
                    data.errorMessage
                )
                Constants.ERROR_CODE_EVENT_NOT_END -> SuniException.EventNotEndException(data.errorMessage)
                Constants.ERROR_CODE_EVENT_IS_TOO_OLD -> SuniException.EventIsTooOldException(data.errorMessage)
                Constants.ERROR_CODE_FAMILY_ALREADY -> SuniException.FamilyAlreadyException(data.errorMessage)
                Constants.ERROR_CODE_FOLLOW_ALREADY -> SuniException.FollowAlreadyException(data.errorMessage)
                Constants.ERROR_CODE_FOLLOW_EXCEED_SEARCH -> SuniException.FollowExceedSearchException(
                    data.errorMessage
                )
                Constants.ERROR_CODE_NO_ON_AIR_BROADCAST -> SuniException.NoOnAirBroadcastException(
                    data.errorMessage
                )
                else -> SuniException.GeneralException(data.errorMessage)
            }
            throw exception
        }
    }
}