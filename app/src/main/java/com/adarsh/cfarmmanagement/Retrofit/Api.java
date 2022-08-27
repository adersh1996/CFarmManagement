package com.adarsh.cfarmmanagement.Retrofit;

import com.adarsh.cfarmmanagement.fragments.EditCustomerProfileFragment;
import com.adarsh.cfarmmanagement.model.AddBreedingDateRoot;
import com.adarsh.cfarmmanagement.model.AddCattle;
import com.adarsh.cfarmmanagement.model.AddMilk;
import com.adarsh.cfarmmanagement.model.AddPregnancyDetailsRoot;
import com.adarsh.cfarmmanagement.model.AddProductRoot;
import com.adarsh.cfarmmanagement.model.BreedingHistoryRoot;
import com.adarsh.cfarmmanagement.model.CustomerLoginRoot;
import com.adarsh.cfarmmanagement.model.CustomerReg;
import com.adarsh.cfarmmanagement.model.CustomerRegRoot;
import com.adarsh.cfarmmanagement.model.CustomerViewProductDetailsRoot;
import com.adarsh.cfarmmanagement.model.CustomerViewProfileRoot;
import com.adarsh.cfarmmanagement.model.DeleteProductRoot;
import com.adarsh.cfarmmanagement.model.DetailedMilkStatusRoot;
import com.adarsh.cfarmmanagement.model.DocChatFarmDetailRoot;
import com.adarsh.cfarmmanagement.model.DocChatHistoryRoot;
import com.adarsh.cfarmmanagement.model.DocLoginRoot;
import com.adarsh.cfarmmanagement.model.DoctorLogOutRoot;
import com.adarsh.cfarmmanagement.model.DoctorProfileRoot;
import com.adarsh.cfarmmanagement.model.DoctorReg;
import com.adarsh.cfarmmanagement.model.DoctorsListRoot;
import com.adarsh.cfarmmanagement.model.EditCattleDetailsRoot;
import com.adarsh.cfarmmanagement.model.EditCustomerProfileRoot;
import com.adarsh.cfarmmanagement.model.EditDoctorProfileRoot;
import com.adarsh.cfarmmanagement.model.EditFarmProfileRoot;
import com.adarsh.cfarmmanagement.model.FarmDocChatRoot;
import com.adarsh.cfarmmanagement.model.FarmLogOutRoot;
import com.adarsh.cfarmmanagement.model.FarmLoginRoot;
import com.adarsh.cfarmmanagement.model.FarmProfileRoot;
import com.adarsh.cfarmmanagement.model.FarmReg;
import com.adarsh.cfarmmanagement.model.HerdStatusRoot;
import com.adarsh.cfarmmanagement.model.MilkStatusMonthlyRoot;
import com.adarsh.cfarmmanagement.model.OrderProductRoot;
import com.adarsh.cfarmmanagement.model.PregnancyDetailRoot;
import com.adarsh.cfarmmanagement.model.PurchaseDetailRoot;
import com.adarsh.cfarmmanagement.model.RecentChatFromDocRoot;
import com.adarsh.cfarmmanagement.model.TagId;
import com.adarsh.cfarmmanagement.model.TagIdRoot;
import com.adarsh.cfarmmanagement.model.TakenVaccineRoot;
import com.adarsh.cfarmmanagement.model.VaccinationHistoryRoot;
import com.adarsh.cfarmmanagement.model.VaccineNotificationRoot;
import com.adarsh.cfarmmanagement.model.ViewCattleDetailsRoot;
import com.adarsh.cfarmmanagement.model.ViewCattleRoot;
import com.adarsh.cfarmmanagement.model.ViewProductDistrictRoot;
import com.adarsh.cfarmmanagement.model.ViewProductFarmRoot;
import com.adarsh.cfarmmanagement.model.ViewProductsRoot;
import com.adarsh.cfarmmanagement.model.ViewVaccineRoot;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    @Multipart
    @POST("customer_reg.php")
    Call<CustomerRegRoot> CUSTOMER_REG_ROOT_CALL(@Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone") RequestBody phone, @Part("state") RequestBody state, @Part("district") RequestBody district, @Part MultipartBody.Part image, @Part("password") RequestBody password, @Part("address") RequestBody address);

    @Multipart
    @POST("doctor_reg.php")
    Call<DoctorReg> DOCTOR_REG_CALL(@Part("name") RequestBody name, @Part("email") RequestBody email, @Part("phone") RequestBody phone, @Part("address") RequestBody address, @Part("password") RequestBody password, @Part("qualification") RequestBody qualification, @Part("experience") RequestBody experience, @Part MultipartBody.Part idPic, @Part MultipartBody.Part proPic);

    @Multipart
    @POST("farm_reg.php")
    Call<FarmReg> FARM_REG_CALL_SELLER(@Part("name") RequestBody name, @Part("phone") RequestBody phone, @Part("mailid") RequestBody email, @Part("address") RequestBody address, @Part("dist") RequestBody district, @Part("states") RequestBody state, @Part("password") RequestBody password, @Part("seller") RequestBody seller, @Part("fssai") RequestBody fssaiLicenseNo, @Part MultipartBody.Part licencePic, @Part MultipartBody.Part proPic);

    @Multipart
    @POST("farm_reg.php")
    Call<FarmReg> FARM_REG_CALL(@Part("name") RequestBody name, @Part("phone") RequestBody phone, @Part("mailid") RequestBody email, @Part("address") RequestBody address, @Part("dist") RequestBody district, @Part("states") RequestBody state, @Part("password") RequestBody password, @Part("seller") RequestBody seller, @Part MultipartBody.Part proPic);

    @FormUrlEncoded
    @POST("farm_login.php")
    Call<FarmLoginRoot> FARM_LOGIN_ROOT_CALL(@Field("phone") String phone,
                                             @Field("password") String password,
                                             @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("customer_login.php")
    Call<CustomerLoginRoot> CUSTOMER_LOGIN_ROOT_CALL(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("doctor_login.php")
    Call<DocLoginRoot> DOC_LOGIN_ROOT_CALL(@Field("phone") String phone, @Field("password") String password, @Field("device_token") String deviceToken);

    @FormUrlEncoded
    @POST("view_cattle_farm.php")
    Call<ViewCattleRoot> VIEW_CATTLE_ROOT_CALL(@Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("add_cattle.php")
    Call<AddCattle> ADD_CATTLE_CALL(@Field("tag_id") String tagId, @Field("farm_id") String farmId, @Field("gender") String gender, @Field("breed") String breed, @Field("dob") String dob, @Field("weight") String weight, @Field("status") String status, @Field("parent_tag") String parentTag, @Field("source") String source);

    @FormUrlEncoded
    @POST("milk_reg.php")
    Call<AddMilk> ADD_MILK_CALL(@Field("farm_id") String farmId, @Field("p_date") String productionDate, @Field("produced") String producedMilk, @Field("spoiled") String spoiledMilk);

    @FormUrlEncoded
    @POST("view_product.php")
    Call<ViewProductDistrictRoot> VIEW_PRODUCT_DISTRICT_ROOT_CALL(@Field("district") String district);

    @FormUrlEncoded
    @POST("view_milk_by_month.php")
    Call<MilkStatusMonthlyRoot> MILK_STATUS_MONTHLY_ROOT_CALL(@Field("month") String month, @Field("farm_id") String farmId);

    @Multipart
    @POST("add_product.php")
    Call<AddProductRoot> ADD_PRODUCT_ROOT_CALL(@Part("product") RequestBody productName,
                                               @Part("price") RequestBody price,
                                               @Part("quantity") RequestBody quantity,
                                               @Part("ingredients") RequestBody ingredients,
                                               @Part("exp_date") RequestBody expiryDate,
                                               @Part("district") RequestBody district,
                                               @Part MultipartBody.Part image,
                                               @Part("farm_id") RequestBody farmId);

    @FormUrlEncoded
    @POST("view_product_farmid.php")
    Call<ViewProductFarmRoot> VIEW_PRODUCT_FARM_ROOT_CALL(@Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("view_milk_by_farmid.php")
    Call<DetailedMilkStatusRoot> DETAILED_MILK_STATUS_ROOT_CALL(@Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("view_doctor.php")
    Call<DoctorsListRoot> DOCTORS_LIST_ROOT_CALL(@Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("view_product_by_productid.php")
    Call<ViewProductsRoot> VIEW_PRODUCTS_ROOT_CALL(@Field("product_id") String productId, @Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("view_cattle_tag.php")
    Call<ViewCattleDetailsRoot> VIEW_CATTLE_DETAILS_ROOT_CALL(@Field("cattle_id") String cattleId, @Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("edit_cattile_profile.php")
    Call<EditCattleDetailsRoot> EDIT_CATTLE_DETAILS_ROOT_CALL(@Field("gender") String gender, @Field("breed") String breed, @Field("dob") String dob, @Field("weight") String weight, @Field("status") String status, @Field("parent_tag") String parentTag, @Field("source") String source, @Field("cattle_id") String cattleId, @Field("tag_id") String tagId, @Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("view_all_tag.php")
    Call<TagIdRoot> TAG_ID_ROOT_CALL(@Field("farm_id") String farmId);

    @FormUrlEncoded
    @POST("vaccine_history.php")
    Call<VaccinationHistoryRoot> VACCINATION_HISTORY_ROOT_CALL(@Field("farm_id") String farmId, @Field("cattle_id") String cattleId);

    @GET("view_vaccine.php")
    Call<ViewVaccineRoot> VIEW_VACCINE_ROOT_CALL();

    @GET("vaccine_taken.php")
    Call<TakenVaccineRoot> TAKEN_VACCINE_ROOT_CALL(@Query("farm_id") String farmId, @Query("cattle_id") String cattleId, @Query("disease") String vaccineName, @Query("date") String date, @Query("tag_id") String tagId);

    @FormUrlEncoded
    @POST("view_product_id.php")
    Call<CustomerViewProductDetailsRoot> CUSTOMER_VIEW_PRODUCT_DETAILS_ROOT_CALL(@Field("product_id") String productId);

    @Multipart
    @POST("farm_doctor_chat.php")
    Call<FarmDocChatRoot> FARM_DOC_CHAT_ROOT_CALL(@Part("farm_id") RequestBody farmId, @Part("doc_id") RequestBody docId, @Part("title") RequestBody title, @Part("message") RequestBody message, @Part MultipartBody.Part image, @Part MultipartBody.Part video, @Part("sender") RequestBody sender);

    @GET("recent_message_from_doctor.php")
    Call<RecentChatFromDocRoot> RECENT_CHAT_FROM_DOC_ROOT_CALL(@Query("farm_id") String farmId, @Query("doc_id") String docId);

    @GET("farm_to_doctor_chat.php")
    Call<DocChatFarmDetailRoot> DOC_CHAT_FARM_DETAIL_ROOT_CALL(@Query("doc_id") String docId);

    @GET("view_doctor_profile.php")
    Call<DoctorProfileRoot> DOCTOR_PROFILE_ROOT_CALL(@Query("doc_id") String docId);

    @GET("view_customer_profile.php")
    Call<CustomerViewProfileRoot> CUSTOMER_VIEW_PROFILE_ROOT_CALL(@Query("id") String customerId);

    @GET("order_product.php")
    Call<OrderProductRoot> ORDER_PRODUCT_ROOT_CALL(@Query("product_id") String productId, @Query("user_id") String UserId, @Query("quantity") String quantity, @Query("total_price") String totalPrice);

    @GET("view_breeding.php")
    Call<BreedingHistoryRoot> BREEDING_HISTORY_ROOT_CALL(@Query("cattile_id") String cattleId, @Query("farm_id") String farmId);

    @GET("add_breeding.php")
    Call<AddBreedingDateRoot> ADD_BREEDING_DATE_ROOT_CALL(@Query("breeding_date") String breedingId, @Query("cattile_id") String cattleId, @Query("farm_id") String farmId);

    @GET("view_chat_history.php")
    Call<DocChatHistoryRoot> DOC_CHAT_HISTORY_ROOT_CALL(@Query("farm_id") String farmId, @Query("doc_id") String docId);

    @GET("view_farm_profile.php")
    Call<FarmProfileRoot> FARM_PROFILE_ROOT_CALL(@Query("farm_id") String farmId);

    @GET("purchase_details.php")
    Call<PurchaseDetailRoot> PURCHASE_DETAIL_ROOT_CALL(@Query("farm_id") String farmId);

    @GET("delete_product.php")
    Call<DeleteProductRoot> DELETE_PRODUCT_ROOT_CALL(@Query("product_id") String productId);

    @GET("doctor_logout.php")
    Call<DoctorLogOutRoot> DOCTOR_LOG_OUT_ROOT_CALL(@Query("doc_id") String docId);

    @GET("farm_logout.php")
    Call<FarmLogOutRoot> FARM_LOG_OUT_ROOT_CALL(@Query("farm_id") String farmId);

    @GET("add_pregnancy.php")
    Call<AddPregnancyDetailsRoot> ADD_PREGNANCY_DETAILS_ROOT_CALL(@Query("breeding_date") String breedingDate, @Query("cattle_id") String cattleId, @Query("farm_id") String farmId, @Query("tag_id") String tagId, @Query("baby_dob") String calfDob);

    @GET("view_pregnancy_details.php")
    Call<PregnancyDetailRoot> PREGNANCY_DETAIL_ROOT_CALL(@Query("cattle_id") String cattleId, @Query("farm_id") String farmId);

    @GET("view_herd_status.php")
    Call<HerdStatusRoot> HERD_STATUS_ROOT_CALL(@Query("farm_id") String farmId);

    @GET("view_booster_dose.php")
    Call<VaccineNotificationRoot> VACCINE_NOTIFICATION_ROOT_CALL(@Query("farm_id") String farmId);

    @Multipart
    @POST("edit_farm_profile.php")
    Call<EditFarmProfileRoot> EDIT_FARM_PROFILE_ROOT_CALL(@Part("name") RequestBody name, @Part("farm_id") RequestBody farmId, @Part("mailid") RequestBody mailId, @Part("phone") RequestBody phone, @Part("address") RequestBody address, @Part("states") RequestBody state, @Part("dist") RequestBody district, @Part("seller") RequestBody seller, @Part("fssai") RequestBody fssaiNo, @Part MultipartBody.Part profilePic, @Part MultipartBody.Part licensePic);

    @Multipart
    @POST("edit_customer_profile.php")
    Call<EditCustomerProfileRoot> EDIT_CUSTOMER_PROFILE_ROOT_CALL(@Part("name") RequestBody name, @Part("email") RequestBody mailId, @Part("phone") RequestBody phone, @Part("state") RequestBody state, @Part("district") RequestBody district, @Part("address") RequestBody address, @Part MultipartBody.Part profilePic, @Part("id") RequestBody customerId);

    @Multipart
    @POST("edit_doctor_profile.php")
    Call<EditDoctorProfileRoot> EDIT_DOCTOR_PROFILE_ROOT_CALL(@Part("name") RequestBody name, @Part("email") RequestBody mailId, @Part("phone") RequestBody phone, @Part("address") RequestBody address, @Part("qualification") RequestBody qualification, @Part("experience") RequestBody experience, @Part MultipartBody.Part profilePic, @Part MultipartBody.Part licensePic, @Part("doc_id") RequestBody docId);

}
