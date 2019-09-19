import helper.EmulatorHelper.*
import java.lang.Exception
import org.apache.commons.io.IOUtils
import java.io.File

const val ENCODING_TYPE = "UTF-8"
var myFile = File("./docs/report.md").printWriter()

fun main(args: Array<String>){
    simpleTryCatch {
        wirelessPreProcess()
        goHome()
        Thread.sleep(500)
        takeAndPullScreenshot("0")
        myFile.println("# First Home Image")
        myFile.println("![](results/0.png)")

        Thread.sleep(500)
        println("The applications was installed: ${installAPK("docs/io.listooUser.apk")}")
        startActivity("io.listooUser","io.listooUser.MainActivity")
        Thread.sleep(1000)
        takeAndPullScreenshot("installedApp")
        myFile.println("# App Installed Image")
        myFile.println("![](results/installedApp.png)")
        unistallAPK("io.listooUser")
        doThe100Events()
        myFile.flush()
        myFile.close()
    }
}

fun executeProcess2(commands: List<String>, commandName: String,onSuccesMessage: String?
                    ,onErrorMessage: String?):List<String>{
    val answer = mutableListOf<String>()
    val pb =ProcessBuilder(commands)
    val spb =pb.start()
    val output = IOUtils.toString(spb.inputStream, ENCODING_TYPE)
    answer.add(output)
    if(!output.startsWith("<?xml")){
        println(output)
    }

    val error = IOUtils.toString(spb.errorStream, ENCODING_TYPE)
    answer.add(error)
    println(error)
    answer.add(commandName)
    if(!error.equals("")){
        throw Exception(error)
    }
    if(!output.startsWith("<?xml>")){
        if (output.startsWith("adb: error") || output.contains("error") || output.contains("Failure")
            || output.contains("Error")) {
            throw Exception(error)
        }
    }
    return answer
}

fun wirelessPreProcess(){
    //TODO aquí ingrese la ip del celular y el puerto
    getDevices()
    connectDeviceWirelessIP("192.168.0.17","5556")
    println("please disconnect the device")
    Thread.sleep(5000)

}

fun doThe100Events(){
    var j =0
    var events = 0
    myFile.println("# 100 Events")
    for(i in 1..100){
        println("mod: ${i%3}")
        when(i%3) {
            1 -> {
                //1
                myFile.println("## Event number: ${++events}")
                goHome()
                Thread.sleep(500)
                takeAndPullScreenshot("${++j}")
                myFile.println("#### ${(j)%6}")
                myFile.println("![](results/$j.png)")
                //2
                tap("177", "2082")
                Thread.sleep(500)
                takeAndPullScreenshot("${++j}")
                myFile.println("#### ${(j)%6}")
                myFile.println("![](results/$j.png)")

            }
            2 -> {
                myFile.println("## Event number: ${++events}")
                goHome()
                Thread.sleep(500)
                //3
                Thread{
                    println("long 1")
                    longTap("124", "1660")
                }.start()
                Thread{
                    println("long 2")
                    longTap("328", "1660")
                }.start()
                Thread{
                    println("long 3")
                    longTap("533", "1660")
                }.start()
                takeAndPullScreenshot("${++j}")
                myFile.println("#### ${j%6}")
                myFile.println("![](results/$j.png)")
                //4
                val wifiStatus = isWifiEnabled()
                println("Wi-Fi status: $wifiStatus")
                takeAndPullScreenshot("${++j}")
                myFile.println("#### ${j%6}")
                myFile.println("Wi-Fi Status: $wifiStatus")
            }
            0 -> {
                myFile.println("#### Event number: ${++events}")
                //5
                takeAndPullScreenshot("${++j}_wirelessDevice")
                myFile.println("#### ${j%6}")
                myFile.println("![](results/${j}_wirelessDevice.png)")
                //6
                //TODO aquí se debe poner el nombre del paquete para los contactos de su celular y el nombre de la actividad inicial para lanzar la aplicación
                try {
                    startActivity("com.samsung.android.contacts","com.samsung.android.contacts.contactslist.PeopleActivity")
                }catch (e: Exception){

                }
                //TODO se deberían ajustar todos los pasos para crear y posteriormente eliminar el nuevo contacto
                Thread.sleep(500)
                tap("960","2025")
                Thread.sleep(300)
                tap("240","621")
                enterInput("newContact")
                goBack()
                tap("240","1165")
                enterInput("1234")
                goBack()
                tap("769","2090")
                Thread.sleep(300)
                takeAndPullScreenshot("${++j}_wirelessDevice")
                myFile.println("#### 6")
                myFile.println("![](results/${j}_wirelessDevice.png)")
                tap("980","197")
                Thread.sleep(200)
                tap("800","185")
                Thread.sleep(200)
                tap("825","2000")
            }
        }
        goBack()
    }
}