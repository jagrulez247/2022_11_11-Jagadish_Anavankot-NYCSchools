package com.education.nycschools.uicomponents.feature.data

import com.education.nycschools.uicomponents.R
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.feature.FeatureConstants.NYC_SCHOOL_DBN_LIST
import kotlinx.parcelize.Parcelize

@Parcelize
class NycSchoolInfoScreen(
    override val tagRes: Int = R.string.nyc_school_info_fragment,
    override val deeplinkHostRes: Int = R.string.nyc_school_info_host
) : BaseFragmentData()

@Parcelize
class NycSchoolSatsScreen(
    override val tagRes: Int = R.string.nyc_school_sats_fragment,
    override val deeplinkHostRes: Int = R.string.nyc_school_info_host
) : BaseFragmentData()

@Parcelize
class NycSchoolDetailScreen(
    override val tagRes: Int = R.string.nyc_school_detail_fragment,
    override val deeplinkHostRes: Int = R.string.nyc_school_info_host
) : BaseFragmentData()