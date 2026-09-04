import { Component, Input } from '@angular/core';
import { NgClass, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [NgClass, DecimalPipe],
  templateUrl: './stat-card.html',
})
export class StatCard {
  @Input() label = '';
  @Input() count: number | null = null;
  @Input() subtitle = '';
  @Input() icon = '';
  @Input() gradient = 'from-indigo-500 to-purple-600';
  @Input() bgLight = 'bg-indigo-50';
  @Input() textColor = 'text-indigo-600';
  @Input() trend: number | null = null;
}
