import {Component, OnInit, ElementRef, ViewChild, AfterViewInit} from '@angular/core';
import { Chart } from 'chart.js';
import {ChartDataService} from '../chart-data.service';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, AfterViewInit {
  @ViewChild('chart') htmlChart: ElementRef;

  chart: any = [];
  chartToggle = false;

  fanState = false;

  constructor(private chartDataService: ChartDataService) {}

  ngOnInit() {
    this.chartDataService.source
      .subscribe(data => {
        if (!this.chartToggle) { return; }

        this.chart.data.labels.push(new Date());

        if (data.type === 'TEMPERATURE') {
          const temperatureData = this.chart.data.datasets.find(({label}) => label === 'Temperature').data;
          if (temperatureData.length >= 20) { temperatureData.shift(); }
          temperatureData.push(data.value);
        }

        if (data.type === 'HUMIDITY') {
          const humidityData = this.chart.data.datasets.find(({label}) => label === 'Humidity').data;
          if (humidityData.length >= 20) { humidityData.shift(); }
          humidityData.push(data.value);
        }

        if (data.type === 'TILT') {
          const tiltData = this.chart.data.datasets.find(({label}) => label === 'Tilt').data;
          if (tiltData.length >= 20) { tiltData.shift(); }
          tiltData.push(data.value);
        }

        if (this.chart.data.labels.length >= 20) { this.chart.data.labels.shift(); }

        this.chart.update();
      });
  }

  ngAfterViewInit() {
    const ctx = this.htmlChart.nativeElement.querySelector('#chart');

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: [],
        datasets: [
          {
            label: 'Temperature',
            data: [],
            borderColor: '#3cba9f',
            fill: false
          },
          {
            label: 'Humidity',
            data: [],
            borderColor: '#ff69f2',
            fill: false
          },
          {
            label: 'Tilt',
            data: [],
            borderColor: '#ff333d',
            fill: false
          }
        ]
      },
      options: {
        legend: {
          display: true,
          position: 'bottom'
        },
        scales: {
          xAxes: [{
            display: true,
            type: 'time',
            time: {
              unit: 'second'
            }
          }],
          yAxes: [{
            display: true
          }],
        }
      }
    });
  }

  toggleFan() {
    if (this.fanState) {
      this.chartDataService.push({ type: 'TOGGLE_FAN', value: 0 });
    }

    if (!this.fanState) {
      this.chartDataService.push({ type: 'TOGGLE_FAN', value: 1 });
    }

    this.fanState = !this.fanState;
  }
}
