import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MsalService } from '@azure/msal-angular';
import { NavbarComponent } from './shared/components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  template: `
    <app-navbar />
    <router-outlet />
  `
})
export class AppComponent implements OnInit {
  constructor(private readonly msal: MsalService) {}

  ngOnInit(): void {
    const account = this.msal.instance.getActiveAccount() ?? this.msal.instance.getAllAccounts()[0];
    if (account) {
      this.msal.instance.setActiveAccount(account);
    }
  }
}
