# Pawming Android — 프로젝트 가이드

## 프로젝트 개요

국가동물보호정보시스템의 공공 Open API를 활용하여, 구조동물 정보와 동물보호센터 정보를 제공하는 **Android 네이티브 앱**입니다. Flutter 버전(포밍)과 동일한 MVP 기능을 Kotlin + Jetpack Compose로 구현합니다.

**타겟 사용자:** 입양 의향과 무관하게 유기동물에 관심 있는 모든 사람 (입양 고려자, 봉사 희망자, 일반 관심자 포함)

---

## 기술 스택

| 구분 | 기술 |
|------|------|
| 언어 | Kotlin |
| UI 프레임워크 | Jetpack Compose |
| 아키텍처 | MVI + Clean Architecture |
| DI | Hilt |
| 네트워크 | Retrofit2 + OkHttp + Kotlin Serialization |
| 비동기 처리 | Kotlin Coroutines + Flow |
| 이미지 로딩 | Coil (Compose 전용) |
| 로컬 저장소 | Room DB 또는 DataStore |
| 지도 SDK | Naver Map SDK for Android |
| Geocoding | Naver Maps Geocoding API |
| 네비게이션 | Navigation3 (Jetpack Navigation3) |
| 백엔드 (추후) | Supabase 또는 Firebase |
| 인증 (추후) | 소셜 로그인 (Google / Kakao) |

---

## 앱 구조 — Bottom Navigation 3탭

- **홈**: 최신 구조동물 피드 (카드 리스트) + 조건별 검색/필터링 (지역, 축종, 품종, 성별, 중성화, 상태 등) → 동물 상세 페이지
- **즐겨찾기**: 로컬 저장된 관심 동물 목록 → 동물 상세 페이지
- **보호센터**: 보호센터 리스트 + 지도뷰 (네이버맵) → 센터 상세 페이지

> 검색/필터 기능은 별도 탭 없이 홈 화면에 통합됩니다.

---

## 아키텍처

### 멀티 모듈 구조

```
:app                    ← NavGraph 조립 · Activity · DI 진입점
:design-system          ← 공통 Composable · 테마 (Color, Type, Shape)
:model                  ← 도메인 모델 (순수 Kotlin)
:domain                 ← Repository 인터페이스 · UseCase
:data                   ← Repository 구현체 · Room · DataStore
:network                ← Retrofit API 서비스 · DTO · NetworkModule
:feature:home:api       ← HomeRoute (NavKey)
:feature:home:impl      ← HomeScreen · homeNavEntries()
:feature:favorites:api  ← FavoritesRoute
:feature:favorites:impl ← FavoritesScreen · favoritesNavEntries()
:feature:shelter:api    ← ShelterRoute
:feature:shelter:impl   ← ShelterScreen · shelterNavEntries()
:feature:animal-detail:api   ← AnimalDetailRoute(desertionNo)
:feature:animal-detail:impl  ← AnimalDetailScreen · animalDetailNavEntries()
:feature:shelter-detail:api  ← ShelterDetailRoute(careRegNo)
:feature:shelter-detail:impl ← ShelterDetailScreen · shelterDetailNavEntries()
```

---

## 네비게이션 구조

> **화면 네비게이션 작업 시 항상 `/navigation-3` skill을 먼저 로드하세요.**

### api/impl 분리 원칙

각 feature 모듈은 두 개의 Gradle 모듈로 분리됩니다.

| 모듈 | 역할 | 포함 내용 |
|------|------|-----------|
| `:feature:xxx:api` | NavKey 소유 | `XxxRoute : NavKey` (`@Serializable`) |
| `:feature:xxx:impl` | Screen + 연결 | `XxxScreen` · `EntryProviderBuilder.xxxNavEntries()` |

**의존 규칙:**
- `:feature:xxx:impl` → `:feature:xxx:api` (자신의 Route import)
- 크로스 피처 이동 (예: HomeScreen → 동물 상세) 시 → `:feature:home:impl`에 `:feature:animal-detail:api`만 추가 (impl 전체 불필요)
- `:app` → `:feature:xxx:impl` (impl이 api를 전이 의존으로 포함)

### NavGraph 조립 패턴

`:app`의 `PawmingNavGraph`가 `EntryProviderBuilder` 확장 함수로 전체를 조립합니다.

```kotlin
// :app/navigation/PawmingNavGraph.kt
NavDisplay(
    entryProvider = entryProvider {
        homeNavEntries(
            onNavigateToAnimalDetail = { desertionNo ->
                homeBackStack.add(AnimalDetailRoute(desertionNo))
            }
        )
        animalDetailNavEntries(onBack = { activeBackStack.removeLastOrNull() })
        // ...
    }
)
```

- 탭별 독립 backstack (`rememberNavBackStack`) → 탭 전환 시 각 탭의 히스토리 유지
- 크로스 피처 이동 콜백은 `:app`이 람다로 제공 → feature 모듈 간 직접 의존 없음

### Navigation3 skill 참조 항목

| 작업 | 참고 레시피 |
|------|------------|
| 새 화면/Route 추가 | `modular-hilt.md` — api/impl 분리 패턴 |
| 탭별 독립 백스택 | `multiple-backstacks.md` |
| 다이얼로그 화면 | `dialog.md` |
| BottomSheet 화면 | `bottomsheet.md` |
| 딥링크 처리 | `deeplinks-basic.md` · `deeplinks-advanced.md` |
| 화면 전환 애니메이션 | `animations.md` |
| 조건부 네비게이션 (로그인 게이트) | `conditional.md` |
| 화면 간 결과 전달 | `results-event.md` · `results-state.md` |
| ViewModel에 인자 전달 | `passingarguments.md` |

---

### 데이터 흐름

> 관련 Skills: `/kotlin-coroutines-structured-concurrency`

```
API → DTO → Mapper → Domain Model → Repository → UseCase → ViewModel (StateFlow) → Composable UI
```

### MVI 상태 관리 패턴

> 관련 Skills: `/kotlin-flow-state-event-modeling` · `/compose-state-holder-ui-split` · `/compose-side-effects` · `/compose-state-hoisting` · `/compose-state-authoring`

- **State**: `data class`로 화면 상태를 단일 불변 객체로 관리
- **Intent (Event)**: `sealed interface`로 사용자 액션/이벤트를 정의
- **SideEffect**: 일회성 이벤트 (토스트, 네비게이션 등)를 `Channel`/`SharedFlow`로 처리
- **ViewModel**: Intent를 받아 State를 변환, `StateFlow`로 UI에 노출
- **Composable**: `collectAsStateWithLifecycle()`로 상태 구독

```kotlin
// State
data class HomeState(
    val animals: List<Animal> = emptyList(),
    val isLoading: Boolean = false,
    val selectedSpecies: Species = Species.ALL,
    val error: String? = null,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 1
)

// Intent
sealed interface HomeIntent {
    data object LoadAnimals : HomeIntent
    data object LoadNextPage : HomeIntent
    data class SelectSpecies(val species: Species) : HomeIntent
    data class ClickAnimal(val desertionNo: String) : HomeIntent
}

// SideEffect
sealed interface HomeSideEffect {
    data class NavigateToDetail(val desertionNo: String) : HomeSideEffect
    data class ShowError(val message: String) : HomeSideEffect
}
```

---

## 네트워크 모델 네이밍 규칙

API 응답 데이터 클래스는 `*NetworkModel` suffix를 사용합니다.

```kotlin
// 공통 응답 래퍼
@Serializable
data class ApiResponseNetworkModel<T>(val response: ResponseBodyNetworkModel<T>)

@Serializable
data class ResponseBodyNetworkModel<T>(
    val header: HeaderNetworkModel,
    val body: BodyNetworkModel<T>?
)
```

### 주요 API 서비스

- `AnimalApiService` — `abandonmentPublic_v2` (구조동물 조회), `sido_v2`, `sigungu_v2`, `shelter_v2`, `kind_v2`
- `ShelterInfoApiService` — `shelterInfo_v2` (동물보호센터 상세 정보)

---

## Composable 파라미터 순서 규칙

`@Composable` 함수의 파라미터는 아래 순서를 따릅니다.

```
필수 데이터 파라미터 → 필수 이벤트 람다(onClick 등) → modifier: Modifier = Modifier → 선택 파라미터 → trailing content/slot 람다
```

```kotlin
// 올바른 예
fun AnimalCard(
    processState: String,       // 필수 데이터
    kindNm: String,             // 필수 데이터
    onClick: () -> Unit,        // 필수 이벤트 람다
    modifier: Modifier = Modifier,
    thumbnail: @Composable BoxScope.() -> Unit  // trailing slot
)

// 잘못된 예 — modifier가 필수 파라미터보다 앞에 위치
fun AnimalCard(
    modifier: Modifier = Modifier,
    thumbnail: @Composable BoxScope.() -> Unit,
    processState: String,
    ...
)
```

---

## 디자인 시스템

### 컬러 — Warm Coral 기반

```kotlin
object PawmingColors {
    val Primary500 = Color(0xFFE8734A)   // 메인 컬러
    val Secondary600 = Color(0xFF6B5D4F) // 세컨더리
    val Success = Color(0xFF1D9E75)
    val Warning = Color(0xFFEF9F27)
    val Danger = Color(0xFFE24B4A)
    val Info = Color(0xFF378ADD)
}
```

### Shape

| 크기 | radius | 용도 |
|------|--------|------|
| extraSmall | 4dp | Badge |
| small | 8dp | Button |
| medium | 12dp | Card |
| large | 16dp | Modal |
| extraLarge | 24dp | Bottom Sheet |

---

## 화면별 핵심 구현 포인트

### 홈 화면

> 관련 Skills: `/compose-state-deferred-reads` (LazyListState 읽기) · `/compose-animations` (AnimatedVisibility) · `/compose-modifier-and-layout-style`

- `LazyColumn` / `LazyVerticalGrid` 무한 스크롤 — `LazyListState.layoutInfo`로 마지막 아이템 도달 감지
- 축종 필터 칩: `FilterChip` 또는 커스텀 `Row`
- 검색/필터 패널: `AnimatedVisibility`로 접기/펼치기
- 연쇄 드롭다운: 시도 → 시군구 → 보호소 / 축종 → 품종 (API 연쇄 호출)
- `ExposedDropdownMenuBox`, `DateRangePicker`

### 즐겨찾기 화면

> 관련 Skills: `/compose-slot-api-pattern` (공통 AnimalCard 재사용)

- Room DB `FavoriteAnimalEntity` 테이블에 저장
- `SwipeToDismiss`로 스와이프 삭제

### 보호센터 화면

> 관련 Skills: `/compose-animations` (탭 전환) · `/compose-side-effects` (마커 탭 → BottomSheet)

- 리스트뷰 / 지도뷰 탭 전환 (`TabRow`)
- 지도뷰: `NaverMap` Composable + `Marker`, 마커 탭 시 `BottomSheet`

### 동물 상세 페이지

> 관련 Skills: `/compose-side-effects` (네비게이션, 전화 걸기 Intent) · `/compose-modifier-and-layout-style`

- 이미지 갤러리: `HorizontalPager` + `PageIndicator` (`popfile1`~`popfile8`)
- 발견장소: Geocoding → 네이버맵 미니맵 (높이 200dp), 실패 시 graceful fallback
- 하단 고정: `BottomAppBar` → 즐겨찾기 토글 + 입양 신청 버튼

---

## 지도 및 Geocoding

- **패키지**: `com.naver.maps:map-sdk`, `com.naver.maps:map-sdk-compose`
- 보호센터 지도: `shelterInfo_v2`의 `lat`/`lng` → `Marker` Composable
- 클러스터링: `TedNaverMapClustering` 라이브러리 활용 고려
- Geocoding 실패 시 지도 미표시 (graceful fallback)

---

## 데이터 소스

- **구조동물 조회 서비스** — 국가동물보호정보시스템 Open API (6개 엔드포인트)
- **동물보호센터 정보조회 서비스** — 동물보호센터 상세 정보 Open API
- **Naver Maps** — 지도 표시 및 Geocoding (발견장소 → 좌표 변환)

---

## 향후 확장 계획

- **Phase 2**: Google/Kakao 소셜 로그인, Supabase/Firebase 즐겨찾기 클라우드 동기화
- **Phase 3**: App Widget, WorkManager + FCM 푸시 알림, 딥링크, 카카오 공유
- **Phase 4**: AI 유사 동물 추천, 통계 대시보드, Wear OS 컴패니언 앱

---

## 참조 Skills (chrisbanes-skills)

코드 작성/리뷰 시 아래 skills를 상황에 맞게 활용하세요.

| 패턴 / 상황 | Skill |
|-------------|-------|
| 화면 추가 · Route · NavGraph · 백스택 · 딥링크 · 애니메이션 | `/navigation-3` |
| StateFlow · SharedFlow · Channel · SideEffect 모델링 | `/kotlin-flow-state-event-modeling` |
| Screen Composable ↔ ViewModel 분리 (MVI 레이어) | `/compose-state-holder-ui-split` |
| LaunchedEffect · DisposableEffect · snapshotFlow · Flow 수집 | `/compose-side-effects` |
| 스크롤·애니메이션 State를 Composition에서 읽을 때 (LazyListState 등) | `/compose-state-deferred-reads` |
| 상태를 어디에 둘지 결정 (로컬 remember vs 호이스팅 vs ViewModel) | `/compose-state-hoisting` |
| 로컬 상태 작성 (`remember { mutableStateOf(...) }` 등) | `/compose-state-authoring` |
| AnimatedVisibility · 전환 애니메이션 · HorizontalPager | `/compose-animations` |
| 재사용 Composable 슬롯 API 설계 (AnimalCard 등 공통 컴포넌트) | `/compose-slot-api-pattern` |
| Modifier 체인 · 레이아웃 API 작성 | `/compose-modifier-and-layout-style` |
| ViewModel의 viewModelScope · 구조적 동시성 | `/kotlin-coroutines-structured-concurrency` |
| Composable 안정성(Stability) · skippability 진단 | `/compose-stability-diagnostics` |
| 리컴포지션 성능 분석 | `/compose-recomposition-performance` |
| Compose UI 테스트 작성 | `/compose-ui-testing-patterns` |