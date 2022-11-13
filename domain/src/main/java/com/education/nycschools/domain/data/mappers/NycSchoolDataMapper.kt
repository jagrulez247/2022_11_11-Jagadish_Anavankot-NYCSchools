package com.education.nycschools.domain.data.mappers

import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolDataItem
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.domain.models.NycSchoolSatsItem
import java.util.*
import javax.inject.Inject

class NycSchoolDataMapper @Inject constructor() {

    fun mapToNycSchoolSatData(apiData: NycSchoolSatsItem?): NycSchoolSatData = NycSchoolSatData(
        dbn = apiData?.dbn ?: "",
        school_name = apiData
            ?.school_name
            ?.trim()
            ?.lowercase(Locale.getDefault())
            ?.split(" ")
            ?.map { word -> word.replaceFirstChar { it.titlecase(Locale.getDefault()) } }
            ?.joinToString(" "),
        sat_critical_reading_avg_score = apiData?.sat_critical_reading_avg_score?.trim(),
        sat_math_avg_score = apiData?.sat_math_avg_score?.trim(),
        sat_writing_avg_score = apiData?.sat_writing_avg_score?.trim(),
        num_of_sat_test_takers = apiData?.num_of_sat_test_takers?.trim(),
        selected = false
    )

    fun mapToNycSchoolData(apiData: NycSchoolDataItem?): NycSchoolData = NycSchoolData(
        dbn = apiData?.dbn ?: "",
        school_name = apiData
            ?.school_name
            ?.trim()
            ?.lowercase(Locale.getDefault())
            ?.split(" ")
            ?.map { word -> word.replaceFirstChar { it.titlecase(Locale.getDefault()) } }
            ?.joinToString(" "),
        total_students = apiData?.total_students?.trim(),
        graduation_rate = apiData?.graduation_rate?.trim(),
        attendance_rate = apiData?.attendance_rate?.trim(),
        overview_paragraph = apiData?.overview_paragraph?.trim(),
        academicopportunities1 = apiData?.academicOpportunities1?.trim(),
        academicopportunities2 = apiData?.academicOpportunities2?.trim(),
        academicopportunities3 = apiData?.academicOpportunities3?.trim(),
        academicopportunities4 = apiData?.academicOpportunities4?.trim(),
        academicopportunities5 = apiData?.academicOpportunities5?.trim(),
        addtl_info1 = apiData?.additional_info1?.trim(),
        extracurricular_activities = apiData?.extracurricular_activities?.trim(),
        psal_sports_boys = apiData?.psal_sports_boys?.trim(),
        psal_sports_girls = apiData?.psal_sports_girls?.trim(),
        eligibility1 = apiData?.eligibility1?.trim(),
        primary_address_line_1 = apiData?.primary_address_line_1?.trim(),
        city = apiData?.city?.trim(),
        state_code = apiData?.state_code?.trim(),
        zip = apiData?.zip?.trim(),
        school_email = apiData?.school_email?.trim(),
        phone_number = apiData?.phone_number?.trim(),
        website = apiData?.website?.trim()
    )

}