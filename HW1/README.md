# 🚀 Rješenje Zadatka 1B - Sistem za Evidenciju Inženjera

**Student:** Ajla Bulić  
**Broj indeksa:** 22130

Ovaj projekat implementira sistem za evidenciju inženjera u programskom jeziku Kotlin.
## 🏗️ Implementacija Struktura

### TACKA 1 - Interface `Osoba`
```kotlin
interface Osoba{
    fun identitet():String 
    fun titula():String
}
```

Trazene su osnovne funkcionalnosti interface-a identitet() i titula() funkcije sto je i dodano.

### TACKA 2 - Osnovna Klasa `Inzenjer`
```kotlin
open class Inzenjer(
    //Odlucila sam da budu privatni atributi klase Inzenjer zbog enkapsulacije i zabrane direktnog pristupa iz koda. 
    //Da je u zadatku trazeno da se polja mogu mijenjati ovo bi predstavljalo problem, pa bi morali jos dodati settere za ono sto planiramo mijenjati...
    //... ali, za nas zadatak ovo funkcionise sasvim ok. :)
    private val ime:String,
    private val prezime:String,
    private val profesionalnaTitula:String,
    private val godineIskustva:Int,
    private val skupEkspertiza:List<String>  
   ):Osoba //nasljeduje interface Osoba
  {
    init{ //validacija podataka, ovaj blok se izvrsava kad god se izvrsava primarni konstruktor klase Inzenjer
        if(godineIskustva<0)                   //dodala sam validaciju proslijedenih podataka i to samo za godine iskustva(da ne budu negativne) i ekspertize(da ne bude prazna lista)
            throw IllegalArgumentException("Ne pravilno navedene godine iskustva")

        if(skupEkspertiza.size==0)
            throw IllegalArgumentException("Skup ekspertiza ne moze biti prazan")  //baca iznimku na lose proslijedene argumente

        println("Konstruisan Inzenjer!")

    }

    fun getGodine():Int {return godineIskustva} //tokom implementacije funkcionalnosti morala sam dodati odredene gettere (jer su mi atributi privatni), i to za godine ...
    fun getEkspertize():List<String>{return skupEkspertiza}  // ... i za skup ekspertiza

    override fun identitet():String{return ime+  " " + prezime}         //overridali iz interface-a, tj. implementirali
    override fun titula():String{return profesionalnaTitula}            //overridali iz interface-a

    override fun toString():String{     //overridali funkciju toString koju poziva print funkcija jer sam zeljela da ljepse formatiram ispis Inzenjera 
        return """--- Profil Inženjera ---  // U ovom formatu ispisa mi je pomogao ChatGPT :D
Ime i prezime: $ime $prezime
Titula: $profesionalnaTitula
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
=============================="""
    }

    fun ispisiInzenjera(){  //wrapper oko funkcije println, ne koristim je u main kodu, ali dodala sam cisto radi zadatka:
    //Treba omogućiti prikaz osnovnih informacija o inženjeru (npr. u metodi za ispis).
        println(this)
    }
}
```

### TACKA 3 - Izvedene Klase
#### 💻 `SoftverskiInzenjer`
```kotlin
class SoftverskiInzenjer(
    private val ime:String,
    private val prezime:String,
    private var godineIskustva:Int,
    private val skupEkspertiza:List<String>, //kao i kod Inzenjera, privatni su atributi 
    private val brojProjekata:Int) : Inzenjer(ime, prezime, "Softverski inzenjer", godineIskustva, skupEkspertiza)//nasljeduje od bazne klase Inzenjer{
    init{ println("Konstruisan softverski inzenjer!")} //radi pracenja ispisa, sta se kad kreira i koji konstruktor se  okine

    fun getProjekti():Int{return brojProjekata} //getter za brojProjekata
    fun jeLiUspjesan():String{if (brojProjekata>5)  return "Uspjesan/na je" else return "Nije uspjesan/na"}

    override fun toString():String{  //override toString radi ljepseg ispisa
        return """--- Profil Inženjera ---
Ime i prezime: $ime $prezime
Titula: Softverski inzenjer
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
Broj projekata: $brojProjekata
=============================="""
    }

}
```

#### ⚡ `InzenjerElektrotehnike`
```kotlin
class InzenjerElektrotehnike(
    private val ime:String,
    private val prezime:String,
    private val godineIskustva:Int,
    private val skupEkspertiza:List<String>, //ista logika za privatne atribute
    private val brojCertifikata:Int) : Inzenjer(ime, prezime, "Inzenjer elektrotehnike", godineIskustva, skupEkspertiza)
    //takoder nasljeduje od bazne klase Inzenjer{
    init{ println("Konstruisan Inzenjer elektrotehnike!")} //radi pracenja procesa konstrukcije  kao i kod softverskog inzenjera 
 
    fun getCertifikati():Int{return brojCertifikata} //getter za certifikate 
    fun jeLiUspjesan():String{if (brojCertifikata>5)  return "Uspjesan/na je" else return "Nije uspjesan/na"}

    override fun toString():String{         //override toStringa radi ljepseg ispisa 
        return """--- Profil Inženjera ---
Ime i prezime: $ime $prezime
Titula: Inzenjer elektrotehnike 
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
Broj certifikata: $brojCertifikata
=============================="""
    }
}```


## OPERACIJE NAD PODACIMA 
### TACKA 4 - Grupisanje Inženjera
```kotlin
fun grupisiInzenjere(listaInzenjera:List<Inzenjer>):Map<String, MutableList<Inzenjer>>{
    val filtriranaLista = listaInzenjera.filter{it -> it.getGodine()>5} //filtriamo samo one sa vise od 5 godina iskustva

    val rezultatFoldanja = filtriranaLista.fold(mutableMapOf<String,MutableList<Inzenjer>>()){ //pozivamo fold sa acc koji defaultno kreiramo i funkcijom u kojoj implementiramo logiku
            acc, inzenjer ->  
        val ekspertize = inzenjer.getEkspertize() //uzmemo listu ekspertiza za trenutnog inzenjera
        ekspertize.forEach { ekspertiza -> //i za svaku ekspertizu ili ako vec postoji u mapi je dodajemo u listu sa kojom je key asociran ili kreiramo novu listu i dodamo je u mapu 
            if (acc.get(ekspertiza)!=null)
                acc[ekspertiza]?.add(inzenjer)
            else
                acc[ekspertiza] = mutableListOf(inzenjer)
        }
        acc
    }
    return rezultatFoldanja //vraca se mapa koja mapira ekspertizu tj string u listu inzenjera koji dijele istu ekspertizu
}
```

### TACKA 5 - Odabir najiskusnijeg sa reduce()
```kotlin
fun reduceInzenjeri(listaInzenjera:List<Inzenjer>):List<Inzenjer>{
    if (listaInzenjera.size==0) //provjera cisto  da se ne pozove sa praznom listom, iako nam ne treba za ono sto mi radimo. Mogla sam isto dodati i u grupisiInzenjere funkciju kao i aggregateInzenjeri funkciju
        throw IllegalArgumentException("Lista ne moze biti prazna!")

    //prvo filtriramo samo softverske i pronalazimo onog sa najvcim godinama iskustva
    val najiskusnijiSoftverski  = listaInzenjera.filter{inzenjer -> inzenjer.titula()=="Softverski inzenjer"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc} 
    //zatim to isto radimo i sa inzenjerima elektrotehnike
    val najiskusnijiElektrotehnicki  = listaInzenjera.filter{inzenjer -> inzenjer.titula()=="Inzenjer elektrotehnike"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc}
    //i na kraju svim ostalima, tj. onima cija je klasa Bazna klasa Inzenjer
    val najiskusnijiOpcenito  = listaInzenjera.filter{inzenjer -> inzenjer.titula()!="Softverski inzenjer" && inzenjer.titula()!="Inzenjer elektrotehnike"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc}


    return listOf(najiskusnijiSoftverski, najiskusnijiElektrotehnicki, najiskusnijiOpcenito) //vracamo listu pronadenih 
}
```


### TACKA 6 - Agregacija sa aggregate()
```kotlin
fun aggregateInzenjeri(lista:List<Inzenjer>):Int {
    val mapa = lista.groupingBy{it.titula()}.aggregate{key, acc:Int?, element, first -> //grupisemo prvo izenjere  po njihovoj  tituli i to koristi aggregate funkcija koja prolazi kroz sve inzenjere. key je trenutna titula(grupisali smo po tituli), acc je akumulator a to ce biti mapa gdje je kljuc isti kao i key za aggregate funkciju, a values je int i predstavlja accumulator.
        if(key == "Softverski inzenjer"){
            if(first) (element as SoftverskiInzenjer).getProjekti() else acc!! +  (element as SoftverskiInzenjer).getProjekti() //ako je prvi element onda je acc null i vratimo sammo projekte prvog, ako je neki drugi sigurni smo da acc nije  null i sabiramo ih 
        }
        else if(key == "Inzenjer elektrotehnike"){//ista logika kao i prethodni slucaj
            if(first)  (element as InzenjerElektrotehnike).getCertifikati() else acc!! + (element as InzenjerElektrotehnike).getCertifikati()
        }else{
            0  //vraca nula za inzenjere ostalih oblasti, jer se ne trazi u zadatku
        }
    }
    return mapa.values.sum()
}

```
### Razlike izmedu fold, reduce i aggregate
Reduce zahtijeva samo funkciju po kojo ce vrsiti kombinovanje elemenata. Nema prosljedivanja akumulatora. Prvi element je u sustini inicijalna vrijednost akumulatora.
lista.reduce { acc, element -> acc + element }
Vraca tip elementa. 

Fold koristi pocetnu vrijednost koju mu proslijedimo i zahtijeva i funkciju po kojoj procesira sve elemente. Vraca onaj tip kakav je i akumulator.

Aggregate funkcija kombinira elemente grupiranih  podataka(Grouping objekat) i radi agregaciju po kljucevima.
lista.groupingBy { it.key }.aggregate { key, acc, element, first -> 
    if(first) initialValue else acc + value
}
Inicijalno je akumulator null. Koristan nam je argument first koji je Boolean i kaze nam je li radimo sa prvim elementom u grupi.
Sintaksa mu je kompleksnija za razliku od fold i reduce.


#### Ukratko:
* reduce nam je koristan kada nam treba suma/min/max 
* fold - mozemo takoder vrsiti sumiranje ili druge transformacije liste
* aggregate - koristan kada radimo sa grupama i kada zelimo da imamo razlicitu logiku u zavisnosti od nekog kljuca(kao sto je kod nas bio slucaj sa titulom)

```Kotlin
//GENERISANO SA AI ALATOM - claude :) 
// reduce: Iz JABUKA dobijaš JABUKU
listOf(🍎, 🍎, 🍎).reduce { ... } → 🍎

// fold: Iz JABUKA dobijaš ŠTA GOD HOĆEŠ
listOf(🍎, 🍎, 🍎).fold(...) { ... } → 🥧  (ili 🧃 ili "3 jabuke" ili bilo šta!)

// aggregate: Iz GRUPA dobijaš MAPU rezultata
listOf(🍎, 🍌, 🍎, 🍌).groupingBy{...}.aggregate{...} → {🍎 → 2, 🍌 → 2}
```

### TESTNI main
```kotlin
fun main() {

    //kreireana lista inzenjera raznih profila
     val listaInzenjera = listOf<Inzenjer>(
        SoftverskiInzenjer("Ajla", "Bulic", 7, listOf("Kotlin", "Android", "Java"), 5),
        SoftverskiInzenjer("Haris", "Kovacevic", 3, listOf("Python", "ML"), 2),
        InzenjerElektrotehnike("Tarik", "Smajic", 9, listOf("Elektronika", "IoT", "C"), 4),
        InzenjerElektrotehnike("Ena", "Dzemic", 11, listOf("Kotlin", "IoT"), 2),
        SoftverskiInzenjer("Sara", "Jevric", 8, listOf("Java", "Spring"), 7),
        Inzenjer("Lejla", "Mujic", "Mehanicki inzenjer", 10, listOf("CAD", "Projektovanje", "C"))
    )


    println("\n=== ISPIS SVIH INZENJERA ===")
    listaInzenjera.forEach{inzenjer -> println(inzenjer)}

    //testiranje grupisanja po ekspertizama sa foldom
    val mapa = grupisiInzenjere(listaInzenjera)
    println("\n=== GRUPISANI INZENJERI PO EKSPERTIZAMA ===")
    check(mapa.values.all{inzenjeri -> inzenjeri.all{inzenjer -> inzenjer.getGodine()>5}}){"Provjera da li fold vraca samo inzenjere sa vise od 5 godina iskustva"}
    //nasla sam sa ChatGPT funkciju check koja uzima izraz koji vraca true ili false i poruku koja ce se prikazati ako uslov nije tacan. Ako nije tacan bacit ce exception tipa IllegalStateException, ako je tacan tok programa nastavlja
    println("Provjera godina iskustva: OK")
    mapa.forEach{ ekspertiza, inzenjeri ->
        println("\n$ekspertiza:")
        inzenjeri.forEach { println("  - ${it.identitet()} (${it.titula()})") }
    }

    //testiranje pronalaska najiskusijeg inzenjera sa reduce
    println("\n=== NAJISKUSNIJI INZENJER ===")
    val najiskusniji = reduceInzenjeri(listaInzenjera)
    check(najiskusniji[0].identitet()=="Sara Jevric"){"Provjera najiskusnijeg softverskog inzenjera nije uspjela!"}
    println("Najiskusniji softverski inzenjer: OK")
    check(najiskusniji[1].identitet()=="Ena Dzemic"){"Provjera najiskusnijeg elektrotehnickog inzenjera nije uspjela!"}
    println("Najiskusniji elektrotehnicki inzenjer: OK")
    check(najiskusniji[2].identitet()=="Lejla Mujic"){"Provjera najiskusnijeg inzenjera drugih kategorija nije uspjela!"}
    println("Najiskusniji inzenjer drugih kategorija: OK")
    for(i in najiskusniji)
        println("\nNajiskusniji ${i.titula()}\n" + i)


    //sabiranje ukupnih vrijednosti sa aggregate
    println("\n=== UKUPNE VRIJEDNOSTI POSTIGNUCA ===")
    val resultAggregate = aggregateInzenjeri(listaInzenjera)
    check(resultAggregate==20){"Greska u provjeri rezultat aggregate funkcije"}
    println("Aggregate check: OK")
    println(resultAggregate)
    println("===============================")

    //testiranje uspjesnosti inzenjera
    listaInzenjera.forEach { inzenjer ->
        if(inzenjer.titula()=="Inzenjer elektrotehnike")
            println(inzenjer.identitet() + ":" + (inzenjer as InzenjerElektrotehnike).jeLiUspjesan())
        else if(inzenjer.titula() =="Softverski inzenjer")
            println(inzenjer.identitet() + ":"+ (inzenjer as SoftverskiInzenjer).jeLiUspjesan())
    }
}
```
