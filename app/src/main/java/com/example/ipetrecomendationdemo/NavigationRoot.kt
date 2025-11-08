package com.example.ipetrecomendationdemo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ipetrecomendationdemo.onboarding.presentation.OnboardingScreen
import com.example.ipetrecomendationdemo.recomendation.domain.PetBasicInfo
import com.example.ipetrecomendationdemo.recomendation.presentation.PetProductsScreen
import com.example.ipetrecomendationdemo.recomendation.presentation.PetRegistrationScreen

// NavigationRoot.kt (apenas o trecho do NavHost e a rota "products")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    // Remove a onboarding do stack
                    navController.navigate("register") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            PetRegistrationScreen(
                onContinueClick = { petInfo ->
                    // Guarda o petInfo no entry atual (register)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("petInfo", petInfo)

                    navController.navigate("products")
                }
            )
        }

        composable("products") {
            // ⚠️ Leia só uma vez e NÃO pop se vier null
            val rememberedPetInfo = remember {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<com.example.ipetrecomendationdemo.recomendation.domain.PetBasicInfo>("petInfo")
            }

            // (Opcional) Limpa a chave depois de ler (evita reutilizar por engano)
            LaunchedEffect(Unit) {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.remove<com.example.ipetrecomendationdemo.recomendation.domain.PetBasicInfo>("petInfo")
            }

            if (rememberedPetInfo == null) {
                // Mostra um fallback amigável (não popa automaticamente)
                androidx.compose.material3.Scaffold(
                    topBar = {
                        androidx.compose.material3.TopAppBar(
                            title = { androidx.compose.material3.Text("Produtos recomendados") },
                            navigationIcon = {
                                androidx.compose.material3.IconButton(
                                    onClick = { navController.popBackStack() }
                                ) {
                                    androidx.compose.material.icons.Icons.Default.KeyboardArrowLeft
                                    androidx.compose.material3.Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.KeyboardArrowLeft,
                                        contentDescription = "Voltar"
                                    )
                                }
                            }
                        )
                    }
                ) { padding ->
                    androidx.compose.foundation.layout.Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.Text("Informações do pet não encontradas.")
                    }
                }
            } else {
                // OK: renderiza normalmente
                PetProductsScreen(
                    navController = navController,
                    petInfo = rememberedPetInfo
                )
            }
        }
    }
}



