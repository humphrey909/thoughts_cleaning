package com.example.thoughts_cleaning.views.start.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.thoughts_cleaning.R
import com.example.thoughts_cleaning.base.MoveEvent
import com.example.thoughts_cleaning.common.TrulyGenericViewModelFactory
import com.example.thoughts_cleaning.databinding.FragmentLoginBinding
import com.example.thoughts_cleaning.databinding.FragmentRecordStateBinding
import com.example.thoughts_cleaning.views.record_problem.vm.fragment.RecordStageFragmentViewModel
import com.example.thoughts_cleaning.views.start.LoginEvent
import com.example.thoughts_cleaning.views.start.vm.fragment.LoginViewModel
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kr.dnx.ble.android.touchcare.api.request.SocialNaverLoginRequestData


class LoginFragment : Fragment() {
    lateinit var mContext: Context

    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: TrulyGenericViewModelFactory

    // 1. View Binding 객체 선언 (null 허용)
    private var _binding: FragmentLoginBinding? = null

    // 2. 뷰가 살아있는 동안에만 접근할 수 있는 Non-null Binding 객체
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = TrulyGenericViewModelFactory(mContext = mContext)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        handleNavigationEvent()
    }

    private fun handleNavigationEvent() {

        viewModel.moveEvent.observe(this.viewLifecycleOwner) {
            if (it is MoveEvent.Login) {
                when(it.moveType) {
//                    LoginEvent.MAIN_WEARER -> goMainWearerView()
//                    LoginEvent.MAIN_GUARDIAN -> goMainGuardianView()
//                    LoginEvent.PERMISSION -> goPermissionView()
//                    LoginEvent.DEBUG -> goDebugView()
//                    LoginEvent.LOGIN_BUTTON -> return@observe
//                    LoginEvent.FIND_BUTTON -> navigate(R.id.action_login_to_find)
//                    LoginEvent.REGISTER_BUTTON -> {
//                        viewModel.profileInfo = MSSocialProfileVO("", "", "", "","", "", "","","")
//                        viewModel.useUsertypeList.add(MSUserTypeVO("X"))
//
//                        val useUsertypeList = viewModel.useUsertypeList.toTypedArray()
//                        navigate(R.id.action_login_to_register_usertype_select, bundleOf("profile" to viewModel.profileInfo, "use_usertype_list" to useUsertypeList))
//                    }
//
//                    LoginEvent.HIDE_KEYBOARD,
//                    LoginEvent.LOGO_IMAGE -> { hideKeyboard() }
//
//                    LoginEvent.INTEGRATED_USER -> showIntegratedUserDialog(false)
//                    LoginEvent.MUST_LOGIN_AS_OWNER -> showMustLoginAsOwnerDialog()
                    LoginEvent.NAVER_LOGIN -> goNaverLogin()
                    LoginEvent.KAKAO_LOGIN -> {
//                        goKakaoLogin()

                    }
//                    LoginEvent.SOCIAL_REGISTER -> goSocialRegister()
//                    LoginEvent.REGISTER_FAIL -> {
//                        showAlreadySaveDialog()
//                    }
//                    LoginEvent.NETWORK_FAIL -> {
//                        showNetWorkErrorAlertDialog()
//                    }
                }
            }
        }


    }

    //네이버 로그인 진행
    private fun goNaverLogin(){
        //네이버 로그인
        context?.let { NaverIdLoginSDK.authenticate(it, oauthLoginCallback) }
    }

    //카카오 로그인 진행
//    private fun goKakaoLogin(){
//
//        // 카카오톡으로 로그인
//        context?.let {
////            var keyHash = Utility.getKeyHash(it)
////            Log.e("TAG", "keyHash $keyHash")
//
//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(it)) {
//                // 카카오톡 앱 로그인 시도
//                UserApiClient.instance.loginWithKakaoTalk(it) { token, error ->
//                    // 로그인 실패 부분
//                    if (error != null) {
//                        Log.e("TAG", "카카오 로그인 실패 $error")
//                        // 사용자가 취소
//                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
//                            //실패했다는 알림
//                            Toast.makeText(context, "카카오 로그인에 실패하였습니다. 처음부터 다시 진행해주세요.", Toast.LENGTH_SHORT).show()
//
//                            return@loginWithKakaoTalk
//                        }
//                        // 다른 오류
//                        else {
//                            //카카오톡 계정으로 로그인 (브라우저 기반)
//                            UserApiClient.instance.loginWithKakaoAccount(it, callback = callback) // 카카오 이메일 로그인
//                        }
//                    }
//                    // 로그인 성공 부분
//                    else if (token != null) {
//                        Log.e("TAG", "로그인 성공 ${token.accessToken}")
//                        getKakaoLoginToken(token)
//                    }
//                }
//
//            } else {
//                //카카오톡 계정으로 로그인 (브라우저 기반)
//                UserApiClient.instance.loginWithKakaoAccount(it, callback = callback)
//            }
//        }
//    }


    //네이버 로그인시 토큰 수집
    val oauthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
//            binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
//            binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
//            binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
//            binding.tvType.text = NaverIdLoginSDK.getTokenType()
//            binding.tvState.text = NaverIdLoginSDK.getState().toString()

            NaverIdLoginSDK.getAccessToken()?.let { Log.d("tvAccessToken", it) } //토큰 정보
            NaverIdLoginSDK.getRefreshToken()?.let { Log.d("tvRefreshToken", it) }
            NaverIdLoginSDK.getExpiresAt()?.let { Log.d("tvExpires", it.toString()) }
            NaverIdLoginSDK.getTokenType()?.let { Log.d("tvType", it) }
            NaverIdLoginSDK.getState()?.let { Log.d("tvState", it.toString()) }

//            Toast.makeText(context,"tvAccessToken:${NaverIdLoginSDK.getAccessToken()}", Toast.LENGTH_SHORT).show()

            //프로필 조회
            NidOAuthLogin().callProfileApi(nidProfileCallback)
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

            errorCode.let { Log.d("errorCode", it) }
            errorDescription?.let { Log.d("errorDescription", it) }

//            Toast.makeText(context, "errorCode: $errorCode, errorDesc: 네이버 로그인에 실패하였습니다. ", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "네이버 로그인에 실패하였습니다. 처음부터 다시 진행해주세요.", Toast.LENGTH_SHORT).show()
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

    //프로필 정보 수집
    val nidProfileCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onSuccess(response: NidProfileResponse) {
//            Toast.makeText(context,"$response",Toast.LENGTH_SHORT).show()

//            Toast.makeText(context, "errorCode: $response, 네이버 로그인에 진행. ", Toast.LENGTH_SHORT).show()

            Log.d("nidProfileCallback", response.toString())
            //NidProfileResponse(resultCode=00, message=success, profile=NidProfile(id=G-vd1nSEOpSFftzjZVWImO4EWNuRibF5Y8e4nd_3dsc, nickname=wlgns****, name=김지훈, email=null, gender=M, age=null, birthday=11-19, profileImage=null, birthYear=1993, mobile=010-7607-1858))
// response.profile

            //email=wlgns1858@gmail.com

            val socialLoginRequestData = SocialNaverLoginRequestData(
                NaverIdLoginSDK.getAccessToken(),
                NaverIdLoginSDK.getRefreshToken(),
                NaverIdLoginSDK.getExpiresAt(),
                response.profile
            )

            //성공시 네이버 로그인 시작
//            viewModel.startNaverLogin(socialLoginRequestData)
        }
        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Log.d("errorCode", errorCode)
            if (errorDescription != null) {
                Log.d("errorCode", errorDescription)
            }

//            Toast.makeText(context, "errorCode: $errorCode, errorDesc: 네이버 로그인 프로필 조회를 실패하였습니다. ", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "네이버 로그인에 실패하였습니다. 처음부터 다시 진행해주세요.", Toast.LENGTH_SHORT).show()
        }
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }

}

















