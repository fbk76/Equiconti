// DENTRO NavHost { ... } sostituisci/aggiungi:

composable(
    route = "owner/{ownerId}",
    arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
) { backStackEntry ->
    val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
    // chiama la tua schermata di dettaglio passando ownerId
    OwnerDetailScreen(nav = nav, ownerId = ownerId)
}

composable(
    route = "owner/{ownerId}/txns",
    arguments = listOf(navArgument("ownerId") { type = NavType.LongType })
) { backStackEntry ->
    val ownerId = backStackEntry.arguments?.getLong("ownerId") ?: 0L
    // chiama la schermata movimenti
