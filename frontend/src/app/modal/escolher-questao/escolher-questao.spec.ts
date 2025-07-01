import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EscolherQuestao } from './escolher-questao';

describe('EscolherQuestao', () => {
  let component: EscolherQuestao;
  let fixture: ComponentFixture<EscolherQuestao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EscolherQuestao]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EscolherQuestao);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
