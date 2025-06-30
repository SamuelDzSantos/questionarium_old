import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliacaoAplicarComponent } from './avaliacao-aplicar.component';

describe('AvaliacaoAplicarComponent', () => {
  let component: AvaliacaoAplicarComponent;
  let fixture: ComponentFixture<AvaliacaoAplicarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliacaoAplicarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvaliacaoAplicarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
