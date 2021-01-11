import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

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
import {RecipeFormComponent} from "./component/forms/RecipeForm/RecipeForm.component";
import {IngredientService} from "./service/IngredientService";

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
  ],
  providers: [RecipeService,
    AuthService,
    IngredientService, {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})

export class AppModule {
}
