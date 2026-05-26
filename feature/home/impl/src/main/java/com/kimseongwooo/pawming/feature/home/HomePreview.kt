package com.kimseongwooo.pawming.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme
import com.kimseongwooo.pawming.model.Animal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private fun fakeAnimal(
    id: String,
    kindNm: String = "[개] 골든 리트리버",
    processState: String = "보호중",
    sexCd: String = "M",
    age: String = "2022(년생)",
    happenPlace: String = "서울특별시 강남구 역삼동"
) = Animal(
    desertionNo = id,
    happenDt = "20240101",
    happenPlace = happenPlace,
    kindCd = "000417000001",
    kindFullNm = kindNm,
    kindNm = kindNm,
    upKindCd = "417000",
    upKindNm = "개",
    colorCd = "갈색",
    age = age,
    weight = "5.5(Kg)",
    sexCd = sexCd,
    neuterYn = "Y",
    rfidCd = "",
    processState = processState,
    endReason = "",
    updTm = "20240101",
    images = persistentListOf(),
    noticeNo = "",
    noticeSdt = "",
    noticeEdt = "",
    specialMark = "",
    sfeSoci = "",
    sfeHealth = "",
    etcBigo = "",
    vaccinationChk = "",
    healthChk = "",
    careRegNo = "111210000096",
    careNm = "서울특별시동물복지지원센터",
    careTel = "02-1234-5678",
    careAddr = "서울특별시 마포구 상암동 481",
    careOwnerNm = "",
    orgNm = "서울특별시",
    adptnTitle = "",
    adptnSDate = "",
    adptnEDate = "",
    adptnConditionLimitTxt = "",
    adptnTxt = "",
    adptnImg = "",
    sprtTitle = "",
    sprtSDate = "",
    sprtEDate = "",
    sprtConditionLimitTxt = "",
    sprtTxt = "",
    sprtImg = "",
    srvcTitle = "",
    srvcSDate = "",
    srvcEDate = "",
    srvcConditionLimitTxt = "",
    srvcTxt = "",
    srvcImg = "",
    evntTitle = "",
    evntSDate = "",
    evntEDate = "",
    evntConditionLimitTxt = "",
    evntTxt = "",
    evntImg = ""
)

private val fakeAnimals = listOf(
    fakeAnimal("001", "[개] 골든 리트리버", processState = "공고중", sexCd = "M"),
    fakeAnimal("002", "[고양이] 코리안 숏헤어", processState = "보호중", sexCd = "F", happenPlace = "경기도 성남시 분당구"),
    fakeAnimal("003", "[개] 믹스견", processState = "보호중", sexCd = "M", age = "2021(년생)"),
    fakeAnimal("004", "[개] 포메라니안", processState = "공고중", sexCd = "F", happenPlace = "부산광역시 해운대구")
).toImmutableList()

// ── HomeScreen ─────────────────────────────────────────────────────────────

@Preview(name = "HomeScreen - 로딩", showBackground = true, device = "spec:width=390dp,height=844dp,dpi=420")
@Composable
private fun HomeScreenLoadingPreview() {
    PawmingTheme {
        HomeScreen(
            uiState = HomeUiState(isLoading = true),
            onIntent = {}
        )
    }
}

@Preview(name = "HomeScreen - 동물 목록", showBackground = true, device = "spec:width=390dp,height=844dp,dpi=420")
@Composable
private fun HomeScreenWithAnimalsPreview() {
    PawmingTheme {
        HomeScreen(
            uiState = HomeUiState(animals = fakeAnimals),
            onIntent = {}
        )
    }
}

@Preview(name = "HomeScreen - 빈 목록 (필터 없음)", showBackground = true, device = "spec:width=390dp,height=844dp,dpi=420")
@Composable
private fun HomeScreenEmptyPreview() {
    PawmingTheme {
        HomeScreen(
            uiState = HomeUiState(isLoading = false),
            onIntent = {}
        )
    }
}

@Preview(name = "HomeScreen - 빈 목록 (필터 적용)", showBackground = true, device = "spec:width=390dp,height=844dp,dpi=420")
@Composable
private fun HomeScreenEmptyWithFilterPreview() {
    PawmingTheme {
        HomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                filterUpkind = "417000",
                filterCareRegNo = "111210000096",
                filterCareNm = "서울특별시동물복지지원센터"
            ),
            onIntent = {}
        )
    }
}

@Preview(name = "HomeScreen - 에러", showBackground = true, device = "spec:width=390dp,height=844dp,dpi=420")
@Composable
private fun HomeScreenErrorPreview() {
    PawmingTheme {
        HomeScreen(
            uiState = HomeUiState(error = "네트워크 연결을 확인해 주세요"),
            onIntent = {}
        )
    }
}
