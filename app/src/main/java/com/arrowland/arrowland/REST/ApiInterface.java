package com.arrowland.arrowland.REST;

import android.app.Notification;

import java.lang.reflect.Member;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Mhack Bautista on 7/20/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("account/register")
    Call<User> userSignUp(@Field("username") String username,
                          @Field("password") String password,
                          @Field("email") String email,
                          @Field("first_name") String first_name,
                          @Field("middle_name") String middle_name,
                          @Field("last_name") String last_name,
                          @Field("address") String address,
                          @Field("contact_number") String contact,
                          @Field("birthday") String birthday,
                          @Field("gender") String gender

                          );

    @FormUrlEncoded
    @POST("account/login")
    Call<User> userLogin(@Field("username") String username,
                         @Field("password") String password
                        );

    @FormUrlEncoded
    @POST("firebase/token")
    Call<User> userToken(@Field("id") int id,
                         @Field("token") String token
                         );

    @GET("fetch_user.php")
    Call<UserList> userList();

    @GET("gallery")
    Call<GalleryList> galleryList();

    // RESERVATION

    @FormUrlEncoded
    @POST("reservation/get_time")
    Call<ReservationList> getReservationTime(@Field("reservation_date") String reservation_date,
                                             @Field("customer_id") int customer_id
                                         );

    @FormUrlEncoded
    @POST("reservation/set")
    Call<ReservationList> setReservation(@Field("reservation_date") String reservation_date,
                                     @Field("reservation_time") String reservation_time,
                                     @Field("package") int mPackage,
                                     @Field("with_coaches") String with_coaches,
                                     @Field("total") double total,
                                     @Field("customer_id") int customer_id

                                     );


    @GET("reservation/list/{id}/{status}")
//    Call<ReservationList> myReservationList(@Query("customer_id") int customer_id,
//                                            @Query("status") String status);
    Call<ReservationList> myReservationList(@Path(value = "id",encoded = true)int customer_id,@Path(value = "status")String status);

    @GET("reservation/count/{id}")
    Call<ReservationList> countMyReservationList(@Path(value = "id",encoded = true)int customer_id);

    @GET("reservation/detail/{id}")
    Call<ReservationList> reservationDetail(@Path(value = "id",encoded = true)int reservation_id);

    @GET("reservation/cancel/{id}")
    Call<ReservationList> reservationCancel(@Path(value = "id",encoded = true)int reservation_id);

    @GET("reservation/total/{id}")
    Call<Reservation> reservationTotal(@Path(value = "id",encoded = true)int reservation_id);

    // MEMBERSHIP

    @GET("membership/set/{id}")
    Call<Membership> setMembership(@Path(value = "id",encoded = true)int customer_id);


    @GET("membership/status/{id}")
    Call<Membership> getMembershipStatus(@Path(value = "id",encoded = true)int customer_id);

    @GET("membership/detail/{id}")
    Call<Membership> getMembershipDetail(@Path(value = "id",encoded = true)int customer_id);

    @FormUrlEncoded
    @POST("membership/check_id/{customer_id}")
    Call<Membership> checkMembershipId(@Path(value = "customer_id",encoded = true)int customer_id,@Field("membership_id")String membership_id);

    // LESSON

    @FormUrlEncoded
    @POST("lesson/set/{id}")
    Call<Lesson> setLesson(@Path(value = "id",encoded = true)int customer_id,@Field("member_status") String member_status);

    @GET("lesson/status/{id}")
    Call<Lesson> getLessonStatus(@Path(value ="id",encoded = true)int customer_id);

    @FormUrlEncoded
    @POST("lesson/get_time/{id}")
    Call<Lesson> getScheduleDate(@Path(value = "id",encoded = true)int lesson_id,@Field("schedule_date")String schedule_date);

    @FormUrlEncoded
    @POST("lesson/{id}/set_schedule")
    Call<Lesson> setSchedule(@Path(value = "id",encoded = true)int lesson_id,
                             @Field("schedule_date")String schedule_date,
                             @Field("schedule_time") String schedule_time
                            );



    @FormUrlEncoded
    @POST("enroll-membership/{id}")
    Call<Lesson> setBoth(@Path(value = "id",encoded = true)int customer_id,
                         @Field("member_status") String member_status
                        );

    @GET("enroll-membership-status/{id}")
    Call<Lesson> statusBoth(@Path(value = "id",encoded = true) int customer_id);



    // PAYMENT

    @GET("payment/check_paid/{reservation_id}")
    Call<Payment> checkReservationPayment(@Path(value = "reservation_id",encoded = true)int reservation_id);
    @GET("payment/{payment_id}/detail")
    Call<Payment> detailPayment(@Path(value = "payment_id",encoded = true)int payment_id);

//    @Multipart
//    @POST("payment/set/{reservation_id}/")
//    Call<Payment> setPayment(
//            @Path(value = "reservation_id",encoded = true)int reservation_id,
//            @Part MultipartBody.Part file,
//            @Part("sender_name")RequestBody sender_name,
//            @Part("reference_number") RequestBody reference_number,
//            @Part("amount") RequestBody amount,
//            @Part("payment_method") RequestBody payment_method
//            );
    @FormUrlEncoded
    @POST("payment/set/{reservation_id}")
    Call<Payment> setPayment(
            @Path(value = "reservation_id",encoded = true) int reservation_id,
            @Field("file") String file,
            @Field("sender_name") String sender_name,
            @Field("reference_number") String reference_number,
            @Field("amount") String amount,
            @Field("payment_method") String payment_method,
            @Field("member_status") String member_status

    );

    @FormUrlEncoded
    @POST("payment/set/cc/{reservation_id}")
    Call<Payment> setCcPayment(@Path(value = "reservation_id") int reservation_id,
                               @Field("stripeToken")String stripeToken,
                               @Field("member_status") String member_status);

    @FormUrlEncoded
    @POST("payment/set/paypal/{reservation_id}")
    Call<Payment> setPaypalPayment(@Path(value = "reservation_id") int reservation_id,
                               @Field("transaction_code")String transaction_code,
                               @Field("member_status") String member_status
                                );




    // NOTIFICATION
    @GET("notification/list/{customer_id}")
    Call<NotificationList> getNotficationList(@Path(value = "customer_id",encoded = true)int customer_id);

    @GET("notification/count/{customer_id}")
    Call<Notifications> countNotification(@Path(value = "customer_id",encoded = true)int customer_id);







}
