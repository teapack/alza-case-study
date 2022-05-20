# Alza Case Study

## Zadání
Vytvořte aplikaci se třemi obrazovkami. Nejsou žádné speciální požadavky na vzhled. Inspirací pro základní rozvržení může být stávající Android aplikace Alza.cz. Zaměřte se však primárně na architekturu aplikace.

### 1. Obrazovka – Homepage
Zobrazte seznam kategorií.

Endpoint:

```
https://www.alza.cz/Services/RestService.svc/v1/floors

GET request
```

### 2. Obrazovka – Seznam produktů
Zobrazte výpis produktů kategorie bez filtrování, podkategorií a případně dalších přepínačů. Na tuto obrazovku se dostanete po volbě příslušné kategorie.

Endpoint:

```
https://www.alza.cz/Services/RestService.svc/v2/products

POST request (id odpovídá id kategorie z první obrazovky)
{
    "filterParameters":{
        "id":18864728,
        "params":[]
    }
}
```

### 3. Obrazovka – Detail produktu
Zobrazte pouze fotografii, název a krátký popis produktu.

Endpoint:

```
https://www.alza.cz/Services/RestService.svc/v13/product/5072141

GET request (id odpovídá id produktu z druhé obrazovky)
```

## Požadavky na technologie
**Níže jsou požadavky na technologie, které se snažte dodržet.** Případně použijte nejbližší alternativu, se kterou máte zkušenosti.
- Napsáno pouze v jazyku Kotlin.
- Vyberte vhodnou architekturu – MVVM, MVI nebo případně jinou.
- Android Architecture Components – zejména ViewModel a LifeCycle.
- Databáze pro cachování dat – SQLDelight, případně Room.
- Kotlin Coroutines (Flow a Channels) a zaměření zejména na jejich vhodné použití. Ideálně nepoužívat LiveData.
- Jetpack Compose namísto Android View v XML.
- Využijte Kotlinx.serialization místo GSON, Moshi apod.
- Použijte OkHttp, Retrofit/Ktor, Picasso/Glide/Coil, Dagger2/Kodein/Koin a případně další běžně používané knihovny.
- **Ne**používejte nic, co by blokovalo možnost rotace displeje (resp. obecně změny konfigurace). Např. `android:screenOrientation`, `android:configChanges` a obdobné způsoby.

Řešení následně odevzdejte ideálně ve formě GIT repositáře v zip souboru s čistým projektem. Pozor tedy na správné nastavení gitignore.

## Poznámka k provolávání endpointů
V rámci ochrany např. proti botům jsou endpointy chráněny proti zneužití. Může se tedy stát, že budou vracet stavový kód 430. Implementace správného řešení této situace je již nad rámec tohoto zadání.

Pokud by k takové situaci došlo, zkuste přidat níže uvedené hlavičky, které by měly snížit četnost výskytů kódu 430, ale ne jim zcela zabránit.

```
accept : application/json
accept-charset : UTF-8
accept-language : cs-CZ
user-agent : okhttp/4.7.0;Google/Android SDK built for x86;10;en_GB;293;9.8.0;0;cz.alza.eshop
```

Případně data namockujte pomocí níže uvedených datových tříd, které odpovídají reponse ze serveru. Pro tento účel můžete použít například `MockWebServer` z `OkHttp`. Díky tomu nebudete potřebovat vůbec přístup k Alza serverům a endpointům.

```
// Kategorie - seznam a jedna z položek - odpovídá v1/floors
data class CategoryList(
    val data: List<CategoryItem>
)

data class CategoryItem(
    val name: String, // Počítače a notebooky
    val id: Int, // 123456
    val imgUrl: String? = null, // https://odkaz na png nebo drawable z paměti zařízení
)
```

```
// Výpis produktů - seznam a jedna z položek - odpovídá v2/products
data class ProductList(
    val data: List<ProductListItem>
)

// Odpovídá kromě položky v v2/products i v13/product
data class ProductListItem(
    val id: Int, // 123456
    val name: String, // Pixel
    val imgUrl: String?, // https://odkaz na png nebo drawable z paměti zařízení
    val price: String?, // 1234 Kč
    val availability: String?, // Skladem > 5 ks
    val canBuy: Boolean // true/false zobrazení tlačítka pro nákup
)
```
