import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultarCabecalhoComponent } from './consultar-cabecalho.component';

describe('ConsultarCabecalhoComponent', () => {
  let component: ConsultarCabecalhoComponent;
  let fixture: ComponentFixture<ConsultarCabecalhoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultarCabecalhoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsultarCabecalhoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
