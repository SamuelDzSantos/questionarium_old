import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvaliacaoCriarComponent } from './avaliacao-criar.component';

describe('AvaliacaoCriarComponent', () => {
  let component: AvaliacaoCriarComponent;
  let fixture: ComponentFixture<AvaliacaoCriarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvaliacaoCriarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AvaliacaoCriarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
