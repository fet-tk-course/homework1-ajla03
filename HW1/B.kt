// FAKULTET ELEKTROTEHNIKE U TUZLI
// ELEKTROTEHNIKA I RACUNARSTVO, RACUNARSTVO I INFORMATIKA
// Ajla Bulic, 22130

//ZADATAK 1B

// TACKA 1. - Konatruisati interface Osoba
interface Osoba{
    fun identitet():String
    fun titula():String
}


// TACKA 2. - Osnovna klasa Inzenjer

open class Inzenjer(
    private val ime:String,
    private val prezime:String,
    private val profesionalnaTitula:String,
    private val godineIskustva:Int,
    private val skupEkspertiza:List<String>
):Osoba
{
    init{ //validacija podataka
        if(godineIskustva<0)
            throw IllegalArgumentException("Ne pravilno navedene godine iskustva")

        if(skupEkspertiza.size==0)
            throw IllegalArgumentException("Skup ekspertiza ne moze biti prazan")

        println("Konstruisan Inzenjer!")

    }

    fun getGodine():Int {return godineIskustva}
    fun getEkspertize():List<String>{return skupEkspertiza}

    override fun identitet():String{return ime+  " " + prezime}         //overridali iz interface-a
    override fun titula():String{return profesionalnaTitula}

    override fun toString():String{
        return """--- Profil Inženjera ---
Ime i prezime: $ime $prezime
Titula: $profesionalnaTitula
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
=============================="""
    }

    fun ispisiInzenjera(){
        println(this)
    }
}


// TACKA 3. - Izvedene klase
class SoftverskiInzenjer(
    private val ime:String,
    private val prezime:String,
    private var godineIskustva:Int,
    private val skupEkspertiza:List<String>,
    private val brojProjekata:Int) : Inzenjer(ime, prezime, "Softverski inzenjer", godineIskustva, skupEkspertiza){
    init{ println("Konstruisan softverski inzenjer!")}

    fun getProjekti():Int{return brojProjekata}

    override fun toString():String{
        return """--- Profil Inženjera ---
Ime i prezime: $ime $prezime
Titula: Softverski inzenjer
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
Broj projekata: $brojProjekata
=============================="""
    }

}

class InzenjerElektrotehnike(
    private val ime:String,
    private val prezime:String,
    private val godineIskustva:Int,
    private val skupEkspertiza:List<String>,
    private val brojCertifikata:Int) : Inzenjer(ime, prezime, "Inzenjer elektrotehnike", godineIskustva, skupEkspertiza){
    init{ println("Konstruisan Inzenjer elektrotehnike!")}

    fun getCertifikati():Int{return brojCertifikata}

    override fun toString():String{
        return """--- Profil Inženjera ---
Ime i prezime: $ime $prezime
Titula: Inzenjer elektrotehnike 
Iskustvo: $godineIskustva godina
Ekspertize: $skupEkspertiza
Broj certifikata: $brojCertifikata
=============================="""
    }
}


// TACKA 4. - grupisanje sa fold()
fun grupisiInzenjere(listaInzenjera:List<Inzenjer>):Map<String, MutableList<Inzenjer>>{
    val filtriranaLista = listaInzenjera.filter{it -> it.getGodine()>5}

    val rezultatFoldanja = filtriranaLista.fold(mutableMapOf<String,MutableList<Inzenjer>>()){
            acc, inzenjer ->
        val ekspertize = inzenjer.getEkspertize()
        ekspertize.forEach { ekspertiza ->
            if (acc.get(ekspertiza)!=null)
                acc[ekspertiza]!!.add(inzenjer)
            else
                acc[ekspertiza] = mutableListOf(inzenjer)
        }
        acc
    }
    return rezultatFoldanja
}


// TACKA 5. - odabir najiskusnijeg sa reduce()
fun reduceInzenjeri(listaInzenjera:List<Inzenjer>):List<Inzenjer>{
    if (listaInzenjera.size==0)
        throw IllegalArgumentException("Lista ne moze biti prazna!")

    val najiskusnijiSoftverski  = listaInzenjera.filter{inzenjer -> inzenjer.titula()=="Softverski inzenjer"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc}
    val najiskusnijiElektrotehnicki  = listaInzenjera.filter{inzenjer -> inzenjer.titula()=="Inzenjer elektrotehnike"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc}
    val najiskusnijiOpcenito  = listaInzenjera.filter{inzenjer -> inzenjer.titula()!="Softverski inzenjer" && inzenjer.titula()!="Inzenjer elektrotehnike"}.reduce{acc, inzenjer-> if(acc.getGodine() < inzenjer.getGodine()) inzenjer else acc}


    return listOf(najiskusnijiSoftverski, najiskusnijiElektrotehnicki, najiskusnijiOpcenito)
}

//TACKA 6. - agregacija sa aggregate
fun aggregateInzenjeri(lista:List<Inzenjer>):Int {
    val mapa = lista.groupingBy{it.titula()}.aggregate{key, acc:Int?, element, first ->
        if(key == "Softverski inzenjer"){
            if(first) (element as SoftverskiInzenjer).getProjekti() else acc!! +  (element as SoftverskiInzenjer).getProjekti()
        }
        else if(key == "Inzenjer elektrotehnike"){
            if(first)  (element as InzenjerElektrotehnike).getCertifikati() else acc!! + (element as InzenjerElektrotehnike).getCertifikati()
        }else{
            0
        }
    }
    return mapa.values.sum()
}

//TESTNI MAIN
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
    check(resultAggregate==20){"Greska u provjeri rezultata aggregate funkcije"}
    println("Aggregate check: OK")
    println(resultAggregate)


}
