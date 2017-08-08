/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SmartStatementTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ConducteurDetailComponent } from '../../../../../../main/webapp/app/entities/conducteur/conducteur-detail.component';
import { ConducteurService } from '../../../../../../main/webapp/app/entities/conducteur/conducteur.service';
import { Conducteur } from '../../../../../../main/webapp/app/entities/conducteur/conducteur.model';

describe('Component Tests', () => {

    describe('Conducteur Management Detail Component', () => {
        let comp: ConducteurDetailComponent;
        let fixture: ComponentFixture<ConducteurDetailComponent>;
        let service: ConducteurService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SmartStatementTestModule],
                declarations: [ConducteurDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ConducteurService,
                    JhiEventManager
                ]
            }).overrideTemplate(ConducteurDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConducteurDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConducteurService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Conducteur(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.conducteur).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
