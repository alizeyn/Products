package ir.alizeyn.products.presentation.products.view

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import ir.alizeyn.products.R
import ir.alizeyn.products.data.network.api.getFirstProduct
import ir.alizeyn.products.data.network.di.NetworkModule
import ir.alizeyn.products.data.network.product.repo.ProductMapper
import ir.alizeyn.products.presentation.products.mapper.ProductUiMapper
import ir.alizeyn.products.utils.launchFragmentInHiltContainer
import ir.alizeyn.products.utils.withRecyclerView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
@MediumTest
@UninstallModules(NetworkModule::class)
@HiltAndroidTest
class TestProductsFragment {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testClickOnProductToShowDetails() {

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ProductsFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(withRecyclerView(R.id.productsRecyclerView).atPosition(0))
            .perform(ViewActions.click())
        val networkProduct = getFirstProduct()
        val product = ProductMapper().map(networkProduct)
        val uiModelProduct = ProductUiMapper().map(product)
        val action = ProductsFragmentDirections.actionProductsFragmentToDetailFragment(uiModelProduct)
        Mockito.verify(navController).navigate(action)
    }
}