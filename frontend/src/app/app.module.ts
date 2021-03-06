import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TableComponent} from './component/table/table.component';
import {FilterPipe} from './component/table/filter.pipe';
import {NgxPaginationModule} from 'ngx-pagination';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {JwtModule} from '@auth0/angular-jwt';
import {RecipeComponent} from './component/recipe/recipe.component';
import {RecipeService} from './service/RecipeService';
import {LoginComponent} from './component/login/login.component';
import {AuthService} from './service/AuthService';
import {TokenInterceptor} from './common/TokenInterceptor';
import {NavBarComponent} from './component/nav-bar/nav-bar.component';
import {RecipeFormComponent} from './component/forms/RecipeForm/RecipeForm.component';
import {IngredientService} from './service/IngredientService';
import {IngredientComponent} from './component/ingredient/ingredient.component';
import {IngredientFormComponent} from './component/forms/IngredientForm/IngredientForm.component';
import {BrewTodayComponent} from './component/brewtoday/brewtoday.component';
import {BrewTodayService} from './service/BrewTodayService';
import {ToolService} from './service/ToolService';
import {ToolComponent} from './component/tool/tool.component';
import {ToolFormComponent} from './component/forms/ToolForm/ToolForm.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HomeComponent} from './component/home/home.component';
import {RegistrationFormComponent} from './component/forms/RegistrationForm/RegistrationForm.component';
import {RecipeDetailComponent} from './component/recipeDetail/recipeDetail.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {BrewerFormComponent} from './component/forms/BrewerForm/brewerForm.component';
import {BrewerService} from './service/BrewerService';
import {IngredientDetailComponent} from './component/ingredientDetail/ingredientDetail.component';
import {BrewComponent} from './component/brew/brew.component';
import {BrewService} from './service/BrewService';
import {BrewFormComponent} from './component/forms/BrewForm/brewForm.component';
import {StorageComponent} from './component/storage/storage.component';
import {StorageService} from './service/StorageService';
import {StorageFormComponent} from './component/forms/StorageForm/StorageForm.component';
import {BrewDetailComponent} from './component/brewDetail/brewDetail.component';
import {HttpErrorInterceptor} from './service/httperrorinterceptor.service';


export function tokenGetter(): string {
  return localStorage.getItem('token');
}

@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    RecipeComponent,
    LoginComponent,
    NavBarComponent,
    RecipeFormComponent,
    FilterPipe,
    IngredientComponent,
    IngredientFormComponent,
    IngredientDetailComponent,
    StorageComponent,
    StorageFormComponent,
    BrewTodayComponent,
    ToolComponent,
    ToolFormComponent,
    HomeComponent,
    RegistrationFormComponent,
    RecipeDetailComponent,
    BrewerFormComponent,
    BrewComponent,
    BrewFormComponent,
    BrewDetailComponent
  ],
  imports: [
    BrowserModule,
    NgxPaginationModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    JwtModule.forRoot({
      config: {
        tokenGetter
      }
    }),
    NgbModule,
    MatProgressSpinnerModule
  ],
  providers: [RecipeService,
    AuthService,
    IngredientService,
    BrewTodayService,
    ToolService,
    BrewerService,
    StorageService,
    BrewService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})

export class AppModule {
}
