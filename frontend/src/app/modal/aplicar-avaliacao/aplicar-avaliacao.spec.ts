import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AplicarAvaliacao } from './aplicar-avaliacao';

describe('AplicarAvaliacao', () => {
  let component: AplicarAvaliacao;
  let fixture: ComponentFixture<AplicarAvaliacao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AplicarAvaliacao]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AplicarAvaliacao);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
