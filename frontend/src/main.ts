import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { APP_INITIALIZER } from '@angular/core';


  
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

