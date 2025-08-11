composable("addOwner") {
    val repo = hiltViewModel<OwnersViewModel>().let { it } // già risolto da Hilt
    AddOwnerScreen(
        nav = navController,
        onSave = { owner ->
            // usa un LaunchedEffect con rememberCoroutineScope se non sei in VM,
            // ma meglio spostare la logica di salvataggio nel ViewModel:
            // vm.save(owner)
        }
    )
}
