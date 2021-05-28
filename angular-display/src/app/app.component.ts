import { Component, OnInit } from '@angular/core';
import * as signalR from '@microsoft/signalr';
import { environment } from 'src/environments/environment';
import { ServerReport } from './app.models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  reportData: ServerReport[] = [];

  ngOnInit() {
    const connection = new signalR.HubConnectionBuilder()
      .configureLogging(signalR.LogLevel.Information)
      .withUrl(environment.baseUrl + 'notifications')
      .build();

    connection.start().then(function () {
      console.log('SignalR Connected!');
    }).catch(function (err) {
      return console.error(err.toString());
    });

    connection.on("OnReceivedReport", (reports: { [serverName: string]: ServerReport }) => {
      this.reportData = Object.values(reports) as ServerReport[];
    });
  }
}
