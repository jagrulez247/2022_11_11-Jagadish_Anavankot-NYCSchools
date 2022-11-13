package com.education.nycschools.domain.models

import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class NycSchoolDataItem(
    @Json(name = "academicopportunities1") val academicOpportunities1: String?,
    @Json(name = "academicopportunities2") val academicOpportunities2: String?,
    @Json(name = "academicopportunities3") val academicOpportunities3: String?,
    @Json(name = "academicopportunities4") val academicOpportunities4: String?,
    @Json(name = "academicopportunities5") val academicOpportunities5: String?,
    @Json(name = "addtl_info1") val additional_info1: String?,
    @Json(name = "attendance_rate") val attendance_rate: String?,
    @Json(name = "bbl") val bbl: String?,
    @Json(name = "bin") val bin: String?,
    @Json(name = "boro") val boro: String?,
    @Json(name = "borough") val borough: String?,
    @Json(name = "building_code") val building_code: String?,
    @Json(name = "bus") val bus: String?,
    @Json(name = "campus_name") val campus_name: String?,
    @Json(name = "census_tract") val census_tract: String?,
    @Json(name = "city") val city: String?,
    @Json(name = "code1") val code1: String?,
    @Json(name = "college_career_rate") val college_career_rate: String?,
    @Json(name = "community_board") val community_board: String?,
    @Json(name = "council_district") val council_district: String?,
    @Json(name = "dbn") val dbn: String?,
    @Json(name = "diplomaendorsements") val diplomaEndorsements: String?,
    @Json(name = "eligibility1") val eligibility1: String?,
    @Json(name = "ell_programs") val ell_programs: String?,
    @Json(name = "end_time") val end_time: String?,
    @Json(name = "extracurricular_activities") val extracurricular_activities: String?,
    @Json(name = "fax_number") val fax_number: String?,
    @Json(name = "finalgrades") val finalGrades: String?,
    @Json(name = "grade9geapplicants1") val grade9GeApplicants1: String?,
    @Json(name = "grade9geapplicantsperseat1") val grade9GeApplicantsPerSeat1: String?,
    @Json(name = "grade9gefilledflag1") val grade9GeFilledFlag1: String?,
    @Json(name = "grade9swdapplicants1") val grade9SwdApplicants1: String?,
    @Json(name = "grade9swdapplicantsperseat1") val grade9SwdApplicantsPerSeat1: String?,
    @Json(name = "grade9swdfilledflag1") val grade9SwdFilledFlag1: String?,
    @Json(name = "grades2018") val grades2018: String?,
    @Json(name = "graduation_rate") val graduation_rate: String?,
    @Json(name = "interest1") val interest1: String?,
    @Json(name = "international") val international: String?,
    @Json(name = "language_classes") val language_classes: String?,
    @Json(name = "latitude") val latitude: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "longitude") val longitude: String?,
    @Json(name = "method1") val method1: String?,
    @Json(name = "neighborhood") val neighborhood: String?,
    @Json(name = "nta") val nta: String?,
    @Json(name = "overview_paragraph") val overview_paragraph: String?,
    @Json(name = "pbat") val pbat: String?,
    @Json(name = "pct_stu_enough_variety") val pct_stu_enough_variety: String?,
    @Json(name = "pct_stu_safe") val pct_stu_safe: String?,
    @Json(name = "phone_number") val phone_number: String?,
    @Json(name = "primary_address_line_1") val primary_address_line_1: String?,
    @Json(name = "program1") val program1: String?,
    @Json(name = "psal_sports_boys") val psal_sports_boys: String?,
    @Json(name = "psal_sports_girls") val psal_sports_girls: String?,
    @Json(name = "school_10th_seats") val school_10th_seats: String?,
    @Json(name = "school_accessibility_description") val school_accessibility_description: String?,
    @Json(name = "school_email") val school_email: String?,
    @Json(name = "school_name") val school_name: String?,
    @Json(name = "school_sports") val school_sports: String?,
    @Json(name = "seats101") val seats101: String?,
    @Json(name = "seats9ge1") val seats9ge1: String?,
    @Json(name = "seats9swd1") val seats9swd1: String?,
    @Json(name = "shared_space") val shared_space: String?,
    @Json(name = "start_time") val start_time: String?,
    @Json(name = "state_code") val state_code: String?,
    @Json(name = "subway") val subway: String?,
    @Json(name = "total_students") val total_students: String?,
    @Json(name = "website") val website: String?,
    @Json(name = "zip") val zip: String?
)

@Keep
@JsonClass(generateAdapter = true)
data class NycSchoolSatsItem(
    @Json(name = "dbn") val dbn: String?,
    @Json(name = "num_of_sat_test_takers") val num_of_sat_test_takers: String?,
    @Json(name = "sat_critical_reading_avg_score") val sat_critical_reading_avg_score: String?,
    @Json(name = "sat_math_avg_score") val sat_math_avg_score: String?,
    @Json(name = "sat_writing_avg_score") val sat_writing_avg_score: String?,
    @Json(name = "school_name") val school_name: String?
)

@Entity(tableName = "nycschools")
data class NycSchoolData(
    @NonNull
    @PrimaryKey
    val dbn: String,
    val school_name: String?,
    val total_students: String?,
    val graduation_rate: String?,
    val attendance_rate: String?,
    val overview_paragraph: String?,
    val academicopportunities1: String?,
    val academicopportunities2: String?,
    val academicopportunities3: String?,
    val academicopportunities4: String?,
    val academicopportunities5: String?,
    val addtl_info1: String?,
    val extracurricular_activities: String?,
    val psal_sports_boys: String?,
    val psal_sports_girls: String?,
    val eligibility1: String?,
    val primary_address_line_1: String?,
    val city: String?,
    val state_code: String?,
    val zip: String?,
    val school_email: String?,
    val phone_number: String?,
    val website: String?
)

@Entity(tableName = "nycschoolsats")
data class NycSchoolSatData(
    @NonNull
    @PrimaryKey
    val dbn: String,
    val school_name: String?,
    val num_of_sat_test_takers: String?,
    val sat_critical_reading_avg_score: String?,
    val sat_math_avg_score: String?,
    val sat_writing_avg_score: String?,
    val selected: Boolean?
)
