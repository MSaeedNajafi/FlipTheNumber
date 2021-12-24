package com.flipthenumber.beta.retrofit;
import com.flipthenumber.beta.model.EventModel;
import com.flipthenumber.beta.model.FriendsModel;
import com.flipthenumber.beta.model.GamePageCounterModel;
import com.flipthenumber.beta.model.GetHomePageDetailsModel;
import com.flipthenumber.beta.model.InviteModel;
import com.flipthenumber.beta.model.InviteToPlay;
import com.flipthenumber.beta.model.ModelAddCoins;
import com.flipthenumber.beta.model.ModelAddDoubleCoins;
import com.flipthenumber.beta.model.ModelAddForReview;
import com.flipthenumber.beta.model.ModelBet;
import com.flipthenumber.beta.model.ModelChatMsg;
import com.flipthenumber.beta.model.ModelCollectReward;
import com.flipthenumber.beta.model.ModelFilterUser;
import com.flipthenumber.beta.model.ModelForgotPassword;
import com.flipthenumber.beta.model.ModelGetCoinsPoints;
import com.flipthenumber.beta.model.ModelGetMsg;
import com.flipthenumber.beta.model.ModelGetPayment;
import com.flipthenumber.beta.model.ModelGetReview;
import com.flipthenumber.beta.model.ModelImageSend;
import com.flipthenumber.beta.model.ModelPayment;
import com.flipthenumber.beta.model.ModelPaymentForAds;
import com.flipthenumber.beta.model.ModelReUseJoker;
import com.flipthenumber.beta.model.ModelRoundTime;
import com.flipthenumber.beta.model.ModelStartTime;
import com.flipthenumber.beta.model.ModeltournamentEntry;
import com.flipthenumber.beta.model.ModeltournamentplayCount;
import com.flipthenumber.beta.model.Modeltournamentplayerlist;
import com.flipthenumber.beta.model.MyRequestModel;
import com.flipthenumber.beta.model.RewardModel;
import com.flipthenumber.beta.model.SendRequestModel;
import com.flipthenumber.beta.model.StoreModel;
import com.flipthenumber.beta.model.UserListModel;
import com.flipthenumber.beta.model.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register")
    Call<UserModel> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("c_password") String c_password,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("guest")
    Call<UserModel> registerGuestUser(
            @Field("name") String name,
            @Field("device_token") String device_token,
            @Field("device_type") String device_type,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("login")
    Call<UserModel> loginUser(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("social_login")
    Call<UserModel> socialLogin(
            @Field("name") String name,
            @Field("email") String email,
            @Field("type") String type,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<UserModel> editProfile(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("gender") String gender,
            @Field("dob") String dob,
            @Field("countryCode") String countryCode,
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<UserModel> editProfileNew(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("name") String name,
            @Field("email") String mobile,
            @Field("password") String password);

    @Multipart
    @POST("image_upload")
    Call<ModelImageSend> updateProfile(@Header("Authorization") String token,
                                       @Part("user_id") RequestBody  id,
                                       @Part MultipartBody.Part image);


    @GET("user_detail")
    Call<UserModel> getUserDetail(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("user_list")
    Call<UserListModel> getAllUser(
            @Header("Authorization") String token,
            @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("myrequest")
    Call<MyRequestModel> getMyRequest(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("cancel_request")
    Call<MyRequestModel> unFriend(
            @Header("Authorization") String token,
            @Field("id") String userId
    );

    @FormUrlEncoded
    @POST("accept_request")
    Call<MyRequestModel> accept_request(
            @Header("Authorization") String token,
            @Field("id") String userId
    );

    @FormUrlEncoded
    @POST("myfrnd_list")
    Call<FriendsModel> getAllFriends(
            @Header("Authorization") String token,
            @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("getfriend")
    Call<InviteModel> getInvitesFriends(
            @Header("Authorization") String token,
            @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("sendrequest")
    Call<SendRequestModel> sendFriendRequest(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("frnd_id") String frnd_id,
            @Field("from") String from);


    @GET("stores")
    Call<StoreModel> getStore();

    @GET("rewards")
    Call<RewardModel> getReward();

    @GET("events")
    Call<EventModel> getEvent();

    @FormUrlEncoded
    @POST("collect_reward")
    Call<ModelCollectReward> collect_reward(
            @Field("user_id") String user_id,
            @Field("total_token") String total_token,
            @Field("total_joker") String total_joker);

    @FormUrlEncoded
    @POST("payment")
    Call<ModelPayment> payment(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("payment_type") String payment_type,
            @Field("type") String type,
            @Field("total") String total,
            @Field("add_type") String add_type,
            @Field("total_add_type") String total_add_type);



    @FormUrlEncoded
    @POST("payment")
    Call<ModelPayment> paymentWith(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("transaction_id") String transaction_id,
            @Field("price") String price,
            @Field("status") String status,
            @Field("payment_type") String payment_type,
            @Field("type") String type,
            @Field("total") String total,
            @Field("add_type") String add_type,
            @Field("total_add_type") String total_add_type);



    @FormUrlEncoded
    @POST("GetHomePageDetails")
    Call<GetHomePageDetailsModel> GetHomePageDetails(
            @Header("Authorization") String token,
            @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("filteruser")
    Call<ModelFilterUser> filteruser(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("type") String type);



    @FormUrlEncoded
    @POST("chat_msg")
    Call<ModelChatMsg> chat_msg(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("reciever_id") String reciever_id,
            @Field("msg") String msg);

    @FormUrlEncoded
    @POST("get_msg")
    Call<ModelGetMsg> get_msg(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("reciever_id") String reciever_id);

    @FormUrlEncoded
    @POST("search_user_list")
    Call<UserListModel> filtergetUser(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("name") String name);

    @FormUrlEncoded
    @POST("add_coins")
    Call<ModelAddCoins> add_coins(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("bet_status") String bet_status,
            @Field("bet_coins") String bet_coins);

    @FormUrlEncoded
    @POST("tournamentplayCount")
    Call<ModeltournamentplayCount> tournamentplayCount(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("tournamentEntry")
    Call<ModeltournamentEntry> tournamentEntry(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id,
            @Field("user_id") String user_id,
            @Field("entry_price") String entry_price);

    @FormUrlEncoded
    @POST("tournamentplayerlist")
    Call<Modeltournamentplayerlist> tournamentplayerlist(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id);

    @FormUrlEncoded
    @POST("roundwinner")
    Call<ModelAddCoins> roundwinner(
            @Header("Authorization") String token,
            @Field("winner_id") String winner_id,
            @Field("losser_id") String losser_id,
            @Field("round") String round,
            @Field("tournament_id") String tournament_id);

    @FormUrlEncoded
    @POST("tournamentfinalprice")
    Call<ModelAddCoins> tournamentfinalprice(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id,
            @Field("winner_id") String winner_id);

    @FormUrlEncoded
    @POST("forget_pswd")
    Call<ModelForgotPassword> forget_pswd(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("re_use_joker")
    Call<ModelReUseJoker> re_use_joker(
            @Header("Authorization") String token,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("add_bet")
    Call<ModelBet> add_bet(
            @Header("Authorization") String token,
            @Field("user_id") String user_id,
            @Field("bet_coins") String bet_coins);

    @FormUrlEncoded
    @POST("tournamentStartTime")
    Call<ModelStartTime> tournamentStartTime(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id);

    @FormUrlEncoded
    @POST("roundStartTime")
    Call<ModelRoundTime> roundStartTime(
            @Header("Authorization") String token,
            @Field("tournament_id") String tournament_id);

    @FormUrlEncoded
    @POST("myrequestAndInvites")
    Call<MyRequestModel> myrequestAndInvites(
            @Header("Authorization") String token,
            @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("inviteFriendsToPlay")
    Call<InviteToPlay> inviteFriendsToPlay(
            @Header("Authorization") String token,
            @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("gamepagecounter")
    Call<GamePageCounterModel> gamepagecounter(
            @Header("Authorization") String token,
            @Field("user_id") String userId);


    @FormUrlEncoded
    @POST("add_payment_by_userid")
    Call<ModelPaymentForAds> add_payment_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("payment") String payment);



    @FormUrlEncoded
    @POST("get_payment_by_userid")
    Call<ModelGetPayment> get_payment_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId);



    @FormUrlEncoded
    @POST("add_coinpoints_by_userid")
    Call<ModelAddDoubleCoins> add_coinpoints_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("coin") String coin,
            @Field("point") String point);


    @FormUrlEncoded
    @POST("get_coinpoints_by_userid")
    Call<ModelGetCoinsPoints> get_coinpoints_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId);




    @FormUrlEncoded
    @POST("add_reviewdialog_by_userid")
    Call<ModelAddForReview> add_reviewdialog_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("from") String from);




    @FormUrlEncoded
    @POST("get_reviewdialog_by_userid")
    Call<ModelGetReview> get_reviewdialog_by_userid(
            @Header("Authorization") String token,
            @Field("user_id") String userId);


}
