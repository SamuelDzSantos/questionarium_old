import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliacaoAplicadasComponent } from './avaliacao-aplicadas.component';

describe('AvaliacaoAplicadasComponent', () => {
  let component: AvaliacaoAplicadasComponent;
  let fixture: ComponentFixture<AvaliacaoAplicadasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliacaoAplicadasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvaliacaoAplicadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
