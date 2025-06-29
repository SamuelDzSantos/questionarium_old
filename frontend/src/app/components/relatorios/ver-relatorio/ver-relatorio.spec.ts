import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VerRelatorioComponent } from './ver-relatorio';

describe('VerRelatorio', () => {
  let component: VerRelatorioComponent;
  let fixture: ComponentFixture<VerRelatorioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerRelatorioComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(VerRelatorioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
