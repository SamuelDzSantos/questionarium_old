import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerarQuestaoComponent } from './gerar-questao.component';

describe('GerarQuestaoComponent', () => {
  let component: GerarQuestaoComponent;
  let fixture: ComponentFixture<GerarQuestaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerarQuestaoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GerarQuestaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
