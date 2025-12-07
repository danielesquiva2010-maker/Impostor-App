```markdown
# Integración rápida de RevealTouchHelper y HighlightAnimator

Estos snippets muestran cómo integrar las utilidades preparadas en un Fragment/Activity. Ajusta los nombres de las vistas y paquetes a tu proyecto.

1) Adjuntar RevealTouchHelper
En el fragment donde muestras la palabra:

// Kotlin (GameFragment.kt)
val revealView = view.findViewById<View>(R.id.wordRevealView) // vista con el texto visible cuando se revela
val hiddenView = view.findViewById<View>(R.id.wordHiddenView) // vista con la máscara u otro estado

val revealHelper = RevealTouchHelper(revealView, hiddenView)
revealHelper.attach()

Asegúrate de que wordRevealView sea el TextView con el texto a mostrar (alpha = 0 por defecto),
y wordHiddenView sea la máscara visible cuando no se revela.

2) Animación highlight al cambiar selección
Crea una vista overlay `highlightView` (por ejemplo, un FrameLayout con borde) en el layout del selector.
Cuando el usuario cambia paquete o idioma o modo:

HighlightAnimator.animateTo(highlightView, newSelectedButton)

3) Excepciones de animación
- Dificultad (Fácil/Medio/Difícil): renderízalas como toggles (selección múltiple). NO llames a HighlightAnimator.animateTo.
- Clases de palabras: si el modo actual es "selección múltiple", evita llamar a animateTo para cada cambio (simplemente togglear estado visual).

4) Carga de emoji packs
Carga el JSON desde assets con una función util:

```kotlin
fun loadEmojiPacks(context: Context): EmojiPacks {
    val json = context.assets.open("emoji_packs.json").bufferedReader().use { it.readText() }
    return Gson().fromJson(json, EmojiPacks::class.java)
}
```

Define las data classes necesarias (EmojiPacks, Pack) para parsear el JSON.

5) Persistencia de pack seleccionado
Guarda el `pack.id` en SharedPreferences y restaura en onCreate/onViewCreated.

6) Pruebas y AAB
- Prueba el reveal manteniendo pulsado (ACTION_DOWN / ACTION_UP).
- Comprueba que al cambiar paquete/idioma/modo el highlight se mueve.
- Verifica que en dificultad y en multi-select de clases NO se anima el highlight.
- Para generar AAB localmente:
  ./gradlew clean bundleRelease
  (El .aab quedará en app/build/outputs/bundle/release/)
```
