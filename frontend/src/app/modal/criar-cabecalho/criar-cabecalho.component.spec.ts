import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriarCabecalhoComponent } from './criar-cabecalho.component';

describe('CriarCabecalhoComponent', () => {
  let component: CriarCabecalhoComponent;
  let fixture: ComponentFixture<CriarCabecalhoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriarCabecalhoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CriarCabecalhoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
