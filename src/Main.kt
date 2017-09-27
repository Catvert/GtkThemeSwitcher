import java.time.LocalDateTime
import java.util.*

object Main {
    private val tooMuchArgs = "Trop d'arguments spécifiés !"
    private val wrongArgs = "Les arguments spécifiés sont incorrects"
    @JvmStatic
    fun main(args: Array<String>) {

        if(args.isEmpty()) {
            println("Aucun argument spécifié ! Ajouter l'argument help pour obtenir l'aide")
            return
        }

        val currentTheme = getCurrentThemeName()

        val firstArg = args[0]
        when(firstArg.toLowerCase()) {
            "help" -> {
                printHelp()
            }
            "get" -> {
                println(currentTheme)
            }
            "set" -> {
                println("Set en cour..")
                if(args.size == 2) {
                    setTheme(args[1])
                }
                else {
                    println(tooMuchArgs)
                }
            }
            "switch" -> {
                println("Switch en cour..")
                if(args.size == 3) {
                    val firstTheme = args[1]
                    val secondTheme = args[2]

                    if(currentTheme.contentEquals(firstTheme)) {
                        setTheme(secondTheme)
                    }
                    else {
                        setTheme(firstTheme)
                    }
                }
                else {
                    println(tooMuchArgs)
                }
            }
            "hourset" -> {
                println("Hourset en cour..")
                if(args.size == 5) {
                    val firstHour = args[1].toIntOrNull()
                    val secondHour = args[2].toIntOrNull()
                    val betweenHoursTheme = args[3]
                    val outHoursTheme = args[4]

                    if(firstHour == null || secondHour == null) {
                        println(wrongArgs)
                        return
                    }

                    val currentHour = LocalDateTime.now()

                    if(currentHour.isAfter(currentHour.withHour(firstHour).withMinute(0))
                            && currentHour.isBefore(currentHour.withHour(secondHour).withMinute(0))) {
                        setTheme(betweenHoursTheme)
                    }
                    else {
                        setTheme(outHoursTheme)
                    }

                }
            }
            else -> {
                println(strArgUnknown(firstArg))
            }
        }
    }

    private fun strArgUnknown(arg: String) = "Argument non reconnu : $arg"

    private fun printHelp() {
        println("-- Aide --")

        println("1 -> get : Permet de retourner le nom du thème actuel\n")

        println("2 -> set [nom du thème] : Permet de changer le thème actuel gtk pour le thème spécifié")
        println("   -> Exemple : set Adapta-Eta\n")

        println("3 -> switch [thème 1] [thème 2] : Permet de switcher de thème entre le premier et le deuxième")
        println("   -> Exemple : switch Adapta-Eta Adapta-Nokto-Eta\n")

        println("4 -> hourset [heure départ] [heure de fin] [thème si entre les heures] [thème sinon] : Permet de spécifier un thème pour une tranche d'heure précise et un thème pour en dehors les heures.")
        println("   -> Exemple : hourset 7 22 Adapta-Eta Adapta-Nokto-Eta")
    }

    private fun setTheme(theme: String) {
        println("Changement du thème actuel pour le thème $theme..")

        execCommand("gsettings set org.gnome.desktop.interface gtk-theme \"$theme\"")

        println("Changement terminé !")
    }

    private fun getCurrentThemeName(): String = execCommand("gsettings get org.gnome.desktop.interface gtk-theme").trim().removeSurrounding("'")

    @Throws(java.io.IOException::class)
    private fun execCommand(command: String): String {
        val s = Scanner(Runtime.getRuntime().exec(command).inputStream).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}