package com.kimseongwooo.pawming.feature.home

import app.cash.turbine.test
import com.kimseongwooo.pawming.domain.usecase.AddFavoriteUseCase
import com.kimseongwooo.pawming.domain.usecase.GetAbandonmentPublicUseCase
import com.kimseongwooo.pawming.domain.usecase.GetFavoriteIdsUseCase
import com.kimseongwooo.pawming.domain.usecase.GetShelterDetailUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSheltersUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSidoUseCase
import com.kimseongwooo.pawming.domain.usecase.GetSigunguUseCase
import com.kimseongwooo.pawming.domain.usecase.RemoveFavoriteUseCase
import com.kimseongwooo.pawming.model.Animal
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.ShelterDetail
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getAbandonmentPublicUseCase: GetAbandonmentPublicUseCase = mockk()
    private val getSheltersUseCase: GetSheltersUseCase = mockk()
    private val getShelterDetailUseCase: GetShelterDetailUseCase = mockk()
    private val getSidoUseCase: GetSidoUseCase = mockk()
    private val getSigunguUseCase: GetSigunguUseCase = mockk()
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase = mockk()
    private val addFavoriteUseCase: AddFavoriteUseCase = mockk()
    private val removeFavoriteUseCase: RemoveFavoriteUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = any(), numOfRows = any()
            )
        } returns Result.success(emptyList())
        every { getFavoriteIdsUseCase() } returns flowOf(emptySet())

        viewModel = createViewModel()
    }

    // ── init ───────────────────────────────────────────────────────────────

    @Test
    fun `loadInitial 성공 시 animals 업데이트 및 로딩 종료`() = runTest {
        val animals = listOf(animalFixture("001"), animalFixture("002"))
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = any(), numOfRows = any()
            )
        } returns Result.success(animals)

        viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.animals.size)
        assertNull(state.error)
    }

    @Test
    fun `loadInitial 실패 시 error 설정 및 로딩 종료`() = runTest {
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = any(), numOfRows = any()
            )
        } returns Result.failure(RuntimeException("네트워크 오류"))

        viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("네트워크 오류", state.error)
        assertTrue(state.animals.isEmpty())
    }

    @Test
    fun `collectFavoriteIds — favoriteIds 상태 반영`() = runTest {
        val ids = setOf("001", "002")
        every { getFavoriteIdsUseCase() } returns flowOf(ids)

        viewModel = createViewModel()

        assertTrue(viewModel.uiState.value.favoriteIds.containsAll(ids))
    }

    // ── LoadMore ───────────────────────────────────────────────────────────

    @Test
    fun `LoadMore 성공 시 animals 추가 및 페이지 증가`() = runTest {
        val page1 = List(20) { animalFixture("p1-$it") }
        val page2 = List(5) { animalFixture("p2-$it") }
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = 1, numOfRows = any()
            )
        } returns Result.success(page1)
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = 2, numOfRows = any()
            )
        } returns Result.success(page2)

        viewModel = createViewModel()
        viewModel.handleIntent(HomeIntent.LoadMore)

        val state = viewModel.uiState.value
        assertEquals(25, state.animals.size)
        assertEquals(2, state.currentPage)
        assertFalse(state.hasMore)
    }

    @Test
    fun `LoadMore — hasMore가 false면 추가 로드 안 함`() = runTest {
        val page1 = List(5) { animalFixture("p1-$it") }
        coEvery {
            getAbandonmentPublicUseCase(
                bgnde = any(), endde = any(), upkind = any(), kind = any(),
                uprCd = any(), orgCd = any(), careRegNo = any(), state = any(),
                neuterYn = any(), pageNo = any(), numOfRows = any()
            )
        } returns Result.success(page1)

        viewModel = createViewModel()
        assertFalse(viewModel.uiState.value.hasMore)

        viewModel.handleIntent(HomeIntent.LoadMore)

        assertEquals(1, viewModel.uiState.value.currentPage)
    }

    // ── Filter ─────────────────────────────────────────────────────────────

    @Test
    fun `ToggleFilter — isFilterOpen 토글`() {
        assertFalse(viewModel.uiState.value.isFilterOpen)
        viewModel.handleIntent(HomeIntent.ToggleFilter)
        assertTrue(viewModel.uiState.value.isFilterOpen)
        viewModel.handleIntent(HomeIntent.ToggleFilter)
        assertFalse(viewModel.uiState.value.isFilterOpen)
    }

    @Test
    fun `SelectUpkind — 새 코드 선택 시 filterUpkind 설정`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectUpkind("417000"))
        assertEquals("417000", viewModel.uiState.value.filterUpkind)
    }

    @Test
    fun `SelectUpkind — 동일 코드 재선택 시 filterUpkind 초기화`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectUpkind("417000"))
        viewModel.handleIntent(HomeIntent.SelectUpkind("417000"))
        assertEquals("", viewModel.uiState.value.filterUpkind)
    }

    @Test
    fun `SelectNeuter — filterNeuter 설정 및 토글`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectNeuter("Y"))
        assertEquals("Y", viewModel.uiState.value.filterNeuter)
        viewModel.handleIntent(HomeIntent.SelectNeuter("Y"))
        assertEquals("", viewModel.uiState.value.filterNeuter)
    }

    @Test
    fun `SelectState — filterState 설정 및 토글`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectState("notice"))
        assertEquals("notice", viewModel.uiState.value.filterState)
        viewModel.handleIntent(HomeIntent.SelectState("notice"))
        assertEquals("", viewModel.uiState.value.filterState)
    }

    @Test
    fun `ResetFilters — 모든 필터 초기화`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectUpkind("417000"))
        viewModel.handleIntent(HomeIntent.SelectNeuter("Y"))
        viewModel.handleIntent(HomeIntent.SelectState("notice"))

        viewModel.handleIntent(HomeIntent.ResetFilters)

        val state = viewModel.uiState.value
        assertEquals("", state.filterUpkind)
        assertEquals("", state.filterNeuter)
        assertEquals("", state.filterState)
        assertEquals("", state.filterCareRegNo)
        assertEquals("", state.filterCareNm)
    }

    // ── SideEffect ─────────────────────────────────────────────────────────

    @Test
    fun `ClickAnimal — NavigateToAnimalDetail SideEffect 발행`() = runTest {
        viewModel.sideEffect.test {
            viewModel.handleIntent(HomeIntent.ClickAnimal("desertionNo-001"))
            assertEquals(
                HomeSideEffect.NavigateToAnimalDetail("desertionNo-001"),
                awaitItem()
            )
        }
    }

    // ── ShelterPicker ──────────────────────────────────────────────────────

    @Test
    fun `ShowShelterPicker — picker 표시 및 sido 로드`() = runTest {
        val sidoList = listOf(Sido("6110000", "서울특별시"))
        coEvery { getSidoUseCase() } returns Result.success(sidoList)

        viewModel.handleIntent(HomeIntent.ShowShelterPicker)

        val state = viewModel.uiState.value
        assertTrue(state.isShowShelterPicker)
        assertEquals(ShelterPickerStep.SIDO, state.shelterPickerStep)
        assertEquals(1, state.sidoList.size)
    }

    @Test
    fun `HideShelterPicker — picker 숨김 및 쿼리 초기화`() {
        viewModel.handleIntent(HomeIntent.HideShelterPicker)

        val state = viewModel.uiState.value
        assertFalse(state.isShowShelterPicker)
        assertEquals("", state.shelterPickerQuery)
    }

    @Test
    fun `UpdateShelterQuery — shelterPickerQuery 업데이트`() {
        viewModel.handleIntent(HomeIntent.UpdateShelterQuery("서울"))
        assertEquals("서울", viewModel.uiState.value.shelterPickerQuery)
    }

    @Test
    fun `SelectSido — SIGUNGU 단계로 이동 및 시군구 로드`() = runTest {
        val sido = Sido("6110000", "서울특별시")
        val sigunguList = listOf(Sigungu("3110000", "종로구", "6110000"))
        coEvery { getSigunguUseCase("6110000") } returns Result.success(sigunguList)

        viewModel.handleIntent(HomeIntent.SelectSido(sido))

        val state = viewModel.uiState.value
        assertEquals(sido, state.selectedSido)
        assertEquals(ShelterPickerStep.SIGUNGU, state.shelterPickerStep)
        assertEquals(1, state.sigunguList.size)
    }

    @Test
    fun `SelectSido — 시군구 없으면 SHELTER 단계로 바로 이동`() = runTest {
        val sido = Sido("5690000", "세종특별자치시")
        val shelters = listOf(shelterFixture("care-001"))
        coEvery { getSigunguUseCase("5690000") } returns Result.success(emptyList())
        coEvery { getSheltersUseCase(uprCd = "5690000", orgCd = "5690000") } returns Result.success(shelters)

        viewModel.handleIntent(HomeIntent.SelectSido(sido))

        val state = viewModel.uiState.value
        assertEquals(ShelterPickerStep.SHELTER, state.shelterPickerStep)
        assertEquals(1, state.shelters.size)
    }

    @Test
    fun `SelectSigungu — SHELTER 단계로 이동 및 보호소 로드`() = runTest {
        val sigungu = Sigungu("3110000", "종로구", "6110000")
        val shelters = listOf(shelterFixture("care-001"), shelterFixture("care-002"))
        coEvery { getSheltersUseCase(uprCd = "6110000", orgCd = "3110000") } returns Result.success(shelters)

        viewModel.handleIntent(HomeIntent.SelectSigungu(sigungu))

        val state = viewModel.uiState.value
        assertEquals(sigungu, state.selectedSigungu)
        assertEquals(ShelterPickerStep.SHELTER, state.shelterPickerStep)
        assertEquals(2, state.shelters.size)
    }

    @Test
    fun `SelectShelter — 필터 설정 및 picker 닫힘 및 재조회`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectShelter("care-001", "서울동물보호센터"))

        val state = viewModel.uiState.value
        assertEquals("care-001", state.filterCareRegNo)
        assertEquals("서울동물보호센터", state.filterCareNm)
        assertFalse(state.isShowShelterPicker)
    }

    @Test
    fun `ClearShelter — 보호소 필터 초기화`() = runTest {
        viewModel.handleIntent(HomeIntent.SelectShelter("care-001", "서울동물보호센터"))
        viewModel.handleIntent(HomeIntent.ClearShelter)

        val state = viewModel.uiState.value
        assertEquals("", state.filterCareRegNo)
        assertEquals("", state.filterCareNm)
    }

    // ── ShelterPickerBack ──────────────────────────────────────────────────

    @Test
    fun `ShelterPickerBack — SIGUNGU에서 뒤로 가면 SIDO로`() = runTest {
        coEvery { getSigunguUseCase(any()) } returns Result.success(listOf(Sigungu("3110000", "종로구", "6110000")))
        viewModel.handleIntent(HomeIntent.SelectSido(Sido("6110000", "서울특별시")))
        assertEquals(ShelterPickerStep.SIGUNGU, viewModel.uiState.value.shelterPickerStep)

        viewModel.handleIntent(HomeIntent.ShelterPickerBack)

        val state = viewModel.uiState.value
        assertEquals(ShelterPickerStep.SIDO, state.shelterPickerStep)
        assertNull(state.selectedSido)
    }

    @Test
    fun `ShelterPickerBack — SHELTER(sigungu 있음)에서 뒤로 가면 SIGUNGU로`() = runTest {
        val sigungu = Sigungu("3110000", "종로구", "6110000")
        coEvery { getSheltersUseCase(any(), any()) } returns Result.success(emptyList())
        viewModel.handleIntent(HomeIntent.SelectSigungu(sigungu))
        assertEquals(ShelterPickerStep.SHELTER, viewModel.uiState.value.shelterPickerStep)

        viewModel.handleIntent(HomeIntent.ShelterPickerBack)

        val state = viewModel.uiState.value
        assertEquals(ShelterPickerStep.SIGUNGU, state.shelterPickerStep)
        assertNull(state.selectedSigungu)
    }

    // ── ShelterDetail ──────────────────────────────────────────────────────

    @Test
    fun `ViewShelterDetail — 상세 정보 로드 성공`() = runTest {
        val detail = shelterDetailFixture("care-001")
        coEvery { getShelterDetailUseCase("care-001") } returns Result.success(detail)

        viewModel.handleIntent(HomeIntent.ViewShelterDetail("care-001"))

        val state = viewModel.uiState.value
        assertNotNull(state.shelterDetail)
        assertEquals("care-001", state.shelterDetail?.careRegNo)
        assertFalse(state.isLoadingShelterDetail)
    }

    @Test
    fun `DismissShelterDetail — shelterDetail 초기화`() = runTest {
        val detail = shelterDetailFixture("care-001")
        coEvery { getShelterDetailUseCase("care-001") } returns Result.success(detail)
        viewModel.handleIntent(HomeIntent.ViewShelterDetail("care-001"))

        viewModel.handleIntent(HomeIntent.DismissShelterDetail)

        assertNull(viewModel.uiState.value.shelterDetail)
    }

    // ── ToggleFavorite ─────────────────────────────────────────────────────

    @Test
    fun `ToggleFavorite — 즐겨찾기 아닌 동물 → addFavoriteUseCase 호출`() = runTest {
        every { getFavoriteIdsUseCase() } returns flowOf(emptySet())
        coEvery { addFavoriteUseCase(any()) } returns Unit
        viewModel = createViewModel()

        val animal = animalFixture("001")
        viewModel.handleIntent(HomeIntent.ToggleFavorite(animal))

        coVerify { addFavoriteUseCase(any()) }
    }

    @Test
    fun `ToggleFavorite — 즐겨찾기 동물 → removeFavoriteUseCase 호출`() = runTest {
        every { getFavoriteIdsUseCase() } returns flowOf(setOf("001"))
        coEvery { removeFavoriteUseCase("001") } returns Unit
        viewModel = createViewModel()

        val animal = animalFixture("001")
        viewModel.handleIntent(HomeIntent.ToggleFavorite(animal))

        coVerify { removeFavoriteUseCase("001") }
    }

    // ── ApplyExternalShelterFilter ─────────────────────────────────────────

    @Test
    fun `ApplyExternalShelterFilter — 필터 설정 및 isFilterOpen`() = runTest {
        viewModel.handleIntent(HomeIntent.ApplyExternalShelterFilter("care-001", "서울동물보호센터"))

        val state = viewModel.uiState.value
        assertEquals("care-001", state.filterCareRegNo)
        assertEquals("서울동물보호센터", state.filterCareNm)
        assertTrue(state.isFilterOpen)
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    private fun createViewModel() = HomeViewModel(
        getAbandonmentPublicUseCase = getAbandonmentPublicUseCase,
        getSheltersUseCase = getSheltersUseCase,
        getShelterDetailUseCase = getShelterDetailUseCase,
        getSidoUseCase = getSidoUseCase,
        getSigunguUseCase = getSigunguUseCase,
        getFavoriteIdsUseCase = getFavoriteIdsUseCase,
        addFavoriteUseCase = addFavoriteUseCase,
        removeFavoriteUseCase = removeFavoriteUseCase
    )

    private fun animalFixture(desertionNo: String = "001") = Animal(
        desertionNo = desertionNo,
        happenDt = "", happenPlace = "", kindCd = "", kindFullNm = "", kindNm = "믹스견",
        upKindCd = "", upKindNm = "", colorCd = "", age = "2020", weight = "",
        sexCd = "M", neuterYn = "Y", rfidCd = "", processState = "보호중", endReason = "",
        updTm = "", images = persistentListOf(), noticeNo = "", noticeSdt = "", noticeEdt = "",
        specialMark = "", sfeSoci = "", sfeHealth = "", etcBigo = "", vaccinationChk = "",
        healthChk = "", careRegNo = "", careNm = "", careTel = "", careAddr = "",
        careOwnerNm = "", orgNm = "", adptnTitle = "", adptnSDate = "", adptnEDate = "",
        adptnConditionLimitTxt = "", adptnTxt = "", adptnImg = "", sprtTitle = "", sprtSDate = "",
        sprtEDate = "", sprtConditionLimitTxt = "", sprtTxt = "", sprtImg = "", srvcTitle = "",
        srvcSDate = "", srvcEDate = "", srvcConditionLimitTxt = "", srvcTxt = "", srvcImg = "",
        evntTitle = "", evntSDate = "", evntEDate = "", evntConditionLimitTxt = "", evntTxt = "",
        evntImg = ""
    )

    private fun shelterFixture(careRegNo: String = "care-001") = Shelter(
        careRegNo = careRegNo,
        careNm = "테스트 보호소"
    )

    private fun shelterDetailFixture(careRegNo: String = "care-001") = ShelterDetail(
        careRegNo = careRegNo, careNm = "테스트 보호소", orgNm = "", divisionNm = "",
        saveTrgtAnimal = "", careAddr = "", jibunAddr = "", lat = null, lng = null,
        dsignationDate = "", careTel = "", dataStdDt = "", weekOprStime = "", weekOprEtime = "",
        weekCellStime = "", weekCellEtime = "", weekendOprStime = "", weekendOprEtime = "",
        weekendCellStime = "", weekendCellEtime = "", closeDay = "", vetPersonCnt = 0,
        specsPersonCnt = 0, medicalCnt = 0, breedCnt = 0, quarantineCnt = 0, feedCnt = 0,
        transCarCnt = 0
    )
}
