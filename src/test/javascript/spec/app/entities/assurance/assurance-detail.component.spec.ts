/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SmartStatementTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AssuranceDetailComponent } from '../../../../../../main/webapp/app/entities/assurance/assurance-detail.component';
import { AssuranceService } from '../../../../../../main/webapp/app/entities/assurance/assurance.service';
import { Assurance } from '../../../../../../main/webapp/app/entities/assurance/assurance.model';

describe('Component Tests', () => {

    describe('Assurance Management Detail Component', () => {
        let comp: AssuranceDetailComponent;
        let fixture: ComponentFixture<AssuranceDetailComponent>;
        let service: AssuranceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SmartStatementTestModule],
                declarations: [AssuranceDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AssuranceService,
                    JhiEventManager
                ]
            }).overrideTemplate(AssuranceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AssuranceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AssuranceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Assurance(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.assurance).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
